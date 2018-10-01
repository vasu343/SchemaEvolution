package slb.employee.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import slb.employee.api.EmployeeService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class EmployeeLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new EmployeeApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new EmployeeApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[EmployeeService])
}

abstract class EmployeeApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Register the Repository
  lazy val employeeRepo = wire[EmployeeRepoImpl]
  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[EmployeeService](wire[EmployeeServiceImpl])

  readSide.register(wire[EmployeeReadProcessor])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: EmployeeSerializerRegistry.type = EmployeeSerializerRegistry


  // Register the employee persistent entity
  persistentEntityRegistry.register(wire[EmployeeEntity])
}
