import java.io.File
import scopt.OParser
import scala.util.Success
import scala.util.Failure
import scala.util.Try
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.util.Calendar

val stateDir = "/var/lib"
val r = scala.util.Random

case class Config(
    mode: Option[String] = None,
    taskName: Option[String] = None,
    taskEffort: Option[Integer] = None,
    taskDescription: Option[String] = None,
    taskDue: Option[Calendar] = None,
    taskIdToDelete: Option[Integer] = None
)

@main def main(args: String*) =
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("pg"),
      head(
        "pg",
        "0.1.0",
        "https://github.com/collinarnett/PersonalGamification"
      ),
      cmd("task")
        .action((_, c) => c.copy(mode = "task"))
        .text("Manage tasks.")
        .children(
          cmd("add")
            .children(
              arg[String]("name")
                .required()
                .action((x, c) => c.copy(taskName = x)),
              arg[String]("description")
                .action((x, c) => c.copy(taskDescription = x)),
              arg[Integer]("effort")
                .action((x, c) => c.copy(taskEffort = x)),
              arg[Calendar]("due")
                .action((x, c) => c.copy(taskDue = x))
            ),
          cmd("delete")
            .children(
              arg[Integer]("id")
                .required()
                .action((x, c) => c.copy(taskIdToDelete = x))
            )
        )
    )

  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => parseArgs(config)
    case _            => None

def parseArgs(config: Config): Unit =
  config.mode match
    case "task" =>
      config.taskDescription, config.taskName, config.taskDue, config.taskEffort
