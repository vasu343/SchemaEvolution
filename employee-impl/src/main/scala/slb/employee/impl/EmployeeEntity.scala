package slb.employee.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonMigration, JsonSerializer, JsonSerializerRegistry}
import slb.employee.api
import com.typesafe.scalalogging.LazyLogging

import scala.collection.immutable
class EmployeeEntity extends PersistentEntity with LazyLogging {

  override type Command = AddEmployee
  override type Event = EmployeeEvent
  override type State = EmployeeState

  override def initialState: EmployeeState = EmployeeState(api.Employee.undefined)

  override def behavior: Behavior = {
    case EmployeeState(_) =>
      Actions().
        onCommand[AddEmployee, api.Employee] {

        case (AddEmployee(employee), ctx, _) =>
          ctx.thenPersist(
            EmployeeAdded(employee)
          ) { _ =>
            ctx.reply(employee)
          }

    }.onEvent {
      case (EmployeeAdded(employee), _) =>
        EmployeeState(employee)

    }
  }
}



object EmployeeSerializerRegistry extends JsonSerializerRegistry with LazyLogging {

  import play.api.libs.json._

  override val serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
    JsonSerializer[AddEmployee],
    JsonSerializer[EmployeeAdded],
    JsonSerializer[api.Employee],
    JsonSerializer[api.AddEmployee],
    JsonSerializer[EmployeeState]
  )

  private val employeeAddedMigration = new JsonMigration(4) {
    override def transform(fromVersion: Int, json: JsObject): JsObject = {
      println(fromVersion)
      if (fromVersion < 2) {
        val obj = json.value("employee").as[JsObject] ++ Json.obj("age"->JsNumber(0)) ++ Json.obj("department"->JsString("CS"))

        Json.obj("employee"->obj)
        //json.fields :+("age", JsNumber(0))
      }
      else if(fromVersion < 3)
        {
          val obj = json.value("employee").as[JsObject] ++ Json.obj("department"->JsString("CS"))

          Json.obj("employee"->obj)
        }

      else if(fromVersion < 4)
      {
        val dept = (JsPath \ "employee"\"dept").read[String].reads(json).get
        val obj = json.value("employee").as[JsObject] ++ Json.obj("department"->JsString(dept))
        Json.obj("employee"->obj)
      }

      else {
        json
      }
    }
  }

  override def migrations = Map[String, JsonMigration](
    classOf[EmployeeAdded].getName -> employeeAddedMigration
  )



}




//object EmployeeSerializerRegistry extends JsonSerializerRegistry {
//  override def serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
//    JsonSerializer[AddEmployee],
//    JsonSerializer[EmployeeAdded],
//    JsonSerializer[api.Employee],
//    JsonSerializer[api.AddEmployee],
//    JsonSerializer[EmployeeState]
//  )
//}