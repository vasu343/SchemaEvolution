package slb.employee.api

import play.api.libs.json.{Format, Json}

case class AddEmployee(
                        id:String,
                        name:String,
                        age: Int,
                        department: String
                      )
object AddEmployee {
  implicit val format: Format[AddEmployee] = Json.format[AddEmployee]
}
