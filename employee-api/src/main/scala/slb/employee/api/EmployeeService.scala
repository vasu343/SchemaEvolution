package slb.employee.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.Service.restCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

object EmployeeService  {
  val TOPIC_NAME = "greetings"
}

/**
  * The employee service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the EmployeeService.
  */
trait EmployeeService extends Service {

  def addEmployee(): ServiceCall[AddEmployee, Employee]
  def getEmployees(): ServiceCall[NotUsed, Seq[Employee]]

  override final def descriptor: Descriptor = {
    import Service._
    import com.lightbend.lagom.scaladsl.api.transport.Method
    named("employee")
      .withCalls(
        restCall(Method.POST, "/api/addEmployee", addEmployee()),
        restCall(Method.GET, "/api/getEmployees", getEmployees())
      )

      .withAutoAcl(true)
  }
}