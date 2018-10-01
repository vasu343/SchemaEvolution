package slb.employee.api


import play.api.libs.json.{Format, Json}

case class Employee(
                     id:String,
                     name:String,
                     age: Int,
                     department: String
                   )
object Employee{
  val undefined = Employee("Unidentified","Unidentified", 0,"Unidentified")
  implicit val format: Format[Employee] = Json.format[Employee]
}