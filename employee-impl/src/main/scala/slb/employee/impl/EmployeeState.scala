package slb.employee.impl

import play.api.libs.json.{Format, Json}
import slb.employee.api

case class EmployeeState(employee: api.Employee)

object EmployeeState {
  implicit val format: Format[EmployeeState] = Json.format
}
