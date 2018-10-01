package slb.employee.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}
import slb.employee.api

trait EmployeeRepo {
  def getEmployees(): Future[Seq[api.Employee]]
}

class EmployeeRepoImpl(session:CassandraSession)(implicit cc:ExecutionContext)
extends EmployeeRepo {
  private val selectSummaryStatement: String = "SELECT * FROM employeeSummary"
  override def getEmployees(): Future[Seq[api.Employee]] = {
    val selectedData = session.selectAll(selectSummaryStatement)
    selectedData.map(
      rows =>
        rows.map(row => {
          val id = row.getString("id")
          val name = row.getString("name")
          val age = row.getInt("age")
          val dept = row.getString("department")
          api.Employee(id,name,age,dept)
        })
    )
  }
}