package slb.employee.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import slb.employee.api._

class EmployeeServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new EmployeeApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[EmployeeService]

  override protected def afterAll() = server.stop()
}
