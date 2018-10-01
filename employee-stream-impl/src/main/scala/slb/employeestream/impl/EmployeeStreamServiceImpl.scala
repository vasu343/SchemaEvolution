package slb.employeestream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import slb.employeestream.api.EmployeeStreamService
import slb.employee.api.EmployeeService

import scala.concurrent.Future

/**
  * Implementation of the EmployeeStreamService.
  */
class EmployeeStreamServiceImpl(employeeService: EmployeeService) extends EmployeeStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(employeeService.hello(_).invoke()))
  }
}
