
lazy val root =  (project in file("."))
    .settings(
      name := "root",
      skip in publish := true,
    )
    .withId("root")
    .settings(commonSettings)
    .aggregate(
      fflinkCheckpointingPvc,
      datamodel,
      sensorDataIngress,
      sensorDataProcess
    )

lazy val flinkCheckpointingPvc = (project in file("flink-checkpointing-pvc"))
      .enablePlugins(CloudflowApplicationPlugin)
      .settings(commonSettings)
      .settings(
          runLocalConfigFile := Some("src/main/resources/local.conf"), 
      )


lazy val sensorDataIngress =  (project in file("entry-point"))
    .enablePlugins(CloudflowAkkaPlugin )
    .settings(commonSettings)
    .settings(
      scalaVersion := "2.12.11",
      runLocalLog4jConfigFile := Some("src/main/resources/log4j.xml"), 

      libraryDependencies ++= Seq(
        "ch.qos.logback"         %  "logback-classic"           % "1.2.3",
      )
    )

lazy val sensorDataProcess =  (project in file("counter"))
    .enablePlugins(CloudflowFlinkPlugin)
    .settings(commonSettings)
    .settings(
      scalaVersion := "2.12.11",
      runLocalLog4jConfigFile := Some("src/main/resources/log4j.xml"), 
      libraryDependencies ++= Seq(
        "ch.qos.logback"         %  "logback-classic"           % "1.2.3",
      )
    )

lazy val datamodel = (project in file ("datamodel"))
    .enablePlugins(CloudflowLibraryPlugin)
    .settings(commonSettings)

lazy val commonSettings = Seq(
  organization := "com.lightbend.cloudflow",
  scalaVersion := "2.12.11",
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
  )
)

dynverSeparator in ThisBuild := "-"

