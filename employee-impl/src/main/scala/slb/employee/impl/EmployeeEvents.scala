package slb.employee.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}
import slb.employee.api

sealed trait EmployeeEvent extends AggregateEvent[EmployeeEvent] {
  def aggregateTag: AggregateEventTag[EmployeeEvent] = EmployeeEvent.Tag
}

object EmployeeEvent {
  val Tag = AggregateEventTag[EmployeeEvent]
}

case class EmployeeAdded(employee: api.Employee) extends EmployeeEvent

object EmployeeAdded {
  implicit val format: Format[EmployeeAdded] = Json.format[EmployeeAdded]
}