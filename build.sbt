val scala3Version = "3.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "PersonalGamification",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % "2.13.1",
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.13.1",
      "com.github.scopt" %% "scopt" % "4.0.1",
      "com.lihaoyi" %% "os-lib" % "0.8.0"
    ),
    assembly / assemblyMergeStrategy := {
      case PathList("module-info.class") => MergeStrategy.discard
      case PathList("META-INF", "versions", xs @ _, "module-info.class") =>
        MergeStrategy.discard
      case x =>
	val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
