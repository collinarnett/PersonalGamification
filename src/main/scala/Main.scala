import java.io.File
import scopt.OParser
import scala.util.Success
import scala.util.Failure
import scala.util.Try
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.util.{Locale, Calendar, GregorianCalendar}
import java.text.SimpleDateFormat
import java.util.Date
import scopt.Read

val stateDir = "/var/lib"
val r = scala.util.Random

def parseTask(
    params: Map[String, Any] = Map(),
    args: List[String]
): Map[String, Any] =
  args match
    case "name" :: name :: tail =>
      parseTask(params ++ Map("name" -> name), tail)
    case "description" :: description :: tail =>
      parseTask(params ++ Map("description" -> description), tail)
    case "effort" :: effort :: tail =>
      parseTask(params ++ Map("effort" -> effort.toInt), tail)
    case "due" :: due :: tail =>
      parseTask(
        params ++ Map("due" -> SimpleDateFormat("yyyy-mm-dd").parse(due)),
        tail
      )
    case Nil => params
    case _   => params

implicit val taskRead: Read[Task] = Read.reads { (s: String) =>
  val args =
    parseTask(args = s.split(",").toList.map(_.split("=").toList).flatten)
  Task(
    args("name").asInstanceOf[String],
    args("description").asInstanceOf[String],
    args("effort").asInstanceOf[Int],
    args("due").asInstanceOf[Date]
  )
}

case class Config(
    mode: Option[String] = None,
    task: Option[Task] = None
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
          opt[Task]("add")
            .required()
            .action((x, c) => c.copy(task = Some(x)))
        )
    )

  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => config
    case _            => None
