package slb.employee.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}
import slb.employee.api

sealed trait EmployeeCommand[R] extends ReplyType[R]

case class AddEmployee(employee: api.Employee) extends EmployeeCommand[api.Employee]

object AddEmployee {
  implicit val format: Format[AddEmployee] = Json.format[AddEmployee]
}
