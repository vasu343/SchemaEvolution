package slb.employee.impl

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class EmployeeEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  private val system = ActorSystem("EmployeeEntitySpec",
    JsonSerializerRegistry.actorSystemSetupFor(EmployeeSerializerRegistry))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withTestDriver(block: PersistentEntityTestDriver[AddEmployee, EmployeeEvent, EmployeeState] => Unit): Unit = {
    val driver = new PersistentEntityTestDriver(system, new EmployeeEntity, "employee-1")
    block(driver)
    driver.getAllIssues should have size 0
  }
}
