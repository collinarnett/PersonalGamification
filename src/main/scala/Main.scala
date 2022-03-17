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

sealed trait TaskArg[A]

case class TaskName(value: String) extends TaskArg[String]
case class TaskEffort(value: Int) extends TaskArg[Int]
case class TaskDescription(value: String) extends TaskArg[String]
case class TaskDue(value: Calendar) extends TaskArg[Calendar]

case class Config(
    mode: Option[String] = None,
    taskName: Option[TaskName] = None,
    taskEffort: Option[TaskEffort] = None,
    taskDescription: Option[TaskDescription] = None,
    taskDue: Option[TaskDue] = None,
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
        .action((_, c) => c.copy(mode = Some("task")))
        .text("Manage tasks.")
        .children(
          cmd("add")
            .children(
              opt[String]("name")
                .required()
                .action((x, c) => c.copy(taskName = Some(TaskName(x)))),
              opt[String]("description")
                .action((x, c) =>
                  c.copy(taskDescription = Some(TaskDescription(x)))
                ),
              opt[Int]("effort")
                .action((x, c) => c.copy(taskEffort = Some(TaskEffort(x)))),
              opt[Calendar]("due")
                .action((x, c) => c.copy(taskDue = Some(TaskDue(x))))
            ),
          cmd("delete")
            .children(
              opt[Int]("id")
                .required()
                .action((x, c) => c.copy(taskIdToDelete = Some(x)))
            )
        )
    )

  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => parseArgs(config)
    case _            => None

def parseArgs(config: Config): Unit =
  config.mode match
    case Some("task") =>
      print(filterTaskArgs(config))

def filterTaskArgs(config: Config) =
  val args: Seq[TaskArg[Any]] = config.productIterator.collect {
    case taskArg: TaskArg[Any] => taskArg
  }.toSeq
