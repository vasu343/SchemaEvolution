package slb.employee.impl

import akka.NotUsed
import slb.employee.api
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.typesafe.scalalogging.LazyLogging
import slb.employee.api.Employee

import scala.collection.immutable.Seq
/**
  * Implementation of the EmployeeService.
  */
class EmployeeServiceImpl(
                           persistentEntityRegistry: PersistentEntityRegistry,
                           employeeRepo: EmployeeRepo
                         )
  extends api.EmployeeService with LazyLogging{

  override def addEmployee(): ServiceCall[api.AddEmployee, api.Employee] =
    ServiceCall { request =>
      val ref = persistentEntityRegistry.refFor[EmployeeEntity](request.id)
      val employee = api.Employee(request.id,request.name, request.age,request.department)
      logger.info(s"Creating Employee with ID: ${request.id} and Name: ${request.name}")
      ref.ask(AddEmployee(employee))
  }

  override def getEmployees(): ServiceCall[NotUsed, scala.Seq[Employee]] =
    ServiceCall[NotUsed, scala.Seq[Employee]] { request =>
      logger.info(s"Getting Employees")
      employeeRepo.getEmployees()
  }
}

