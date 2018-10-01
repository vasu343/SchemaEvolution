package slb.employee.impl

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.concurrent.{ExecutionContext, Future}

class EmployeeReadProcessor(session: CassandraSession, readSide: CassandraReadSide) (
                           implicit cc: ExecutionContext
) extends ReadSideProcessor[EmployeeEvent] {

  private val createTable =
    """CREATE TABLE IF NOT EXISTS employeeSummary (
      |id text, name text, age int,department text,
      |PRIMARY KEY(id)
      |)""".stripMargin
  private var insertEventStatement: PreparedStatement = _
  // TODO: xxxEventStatement: PreparedStatement = _

  override def aggregateTags: Set[AggregateEventTag[EmployeeEvent]] = {
    Set(EmployeeEvent.Tag)
  }
  override def buildHandler(): ReadSideProcessor.ReadSideHandler[EmployeeEvent] =
    readSide
      .builder[EmployeeEvent]("employee_offset")
      .setGlobalPrepare(prepareTable _)
      .setPrepare(_ => prepareMessage())
      .setEventHandler[EmployeeAdded](ese => processEmployeeAdded(ese.event))
      .build()

  private def prepareTable(): Future[Done] = session.executeCreateTable(createTable)

  private def prepareMessage(): Future[Done] = {
    for {
      insertEvent <- session.prepare("""
        INSERT INTO employeeSummary(
        id, name, age, department
        ) VALUES (?,?,?,?)
        """.stripMargin)

    } yield {
      insertEventStatement = insertEvent
      Done
    }
  }

  private def processEmployeeAdded(event: EmployeeAdded): Future[List[BoundStatement]] = {
    val binding = insertEventStatement.bind
    binding.setString("id", event.employee.id)
    binding.setString("name", event.employee.name)
    binding.setInt("age", event.employee.age)
    binding.setString("department", event.employee.department)
    Future.successful(List(binding))
  }

}
