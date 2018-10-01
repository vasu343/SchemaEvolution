
organization in ThisBuild := "slb"
version in ThisBuild := "1"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

// clean cassandra database
lagomCassandraCleanOnStart in ThisBuild := false

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"

lazy val `employee` = (project in file("."))
  .aggregate(`employee-api`, `employee-impl`, `employee-stream-api`, `employee-stream-impl`)

lazy val `employee-api` = (project in file("employee-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `employee-impl` = (project in file("employee-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      scalaLogging,
      logbackClassic
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`employee-api`)

lazy val `employee-stream-api` = (project in file("employee-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `employee-stream-impl` = (project in file("employee-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`employee-stream-api`, `employee-api`)

lagomUnmanagedServices in ThisBuild := Map(
  "elastic-search" -> "http://127.0.0.1:9200",
  "cas_native"     -> "http://localhost:9042"
)
