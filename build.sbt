val scala3Version = "3.0.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "PersonalGamification",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
      "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % "2.12.5",
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.12.5",
      "com.github.scopt" %% "scopt" % "4.0.1"
    )
  )
