package slb.employeestream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import slb.employeestream.api.EmployeeStreamService
import slb.employee.api.EmployeeService
import com.softwaremill.macwire._

class EmployeeStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new EmployeeStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new EmployeeStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[EmployeeStreamService])
}

abstract class EmployeeStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[EmployeeStreamService](wire[EmployeeStreamServiceImpl])

  // Bind the EmployeeService client
  lazy val employeeService = serviceClient.implement[EmployeeService]
}
