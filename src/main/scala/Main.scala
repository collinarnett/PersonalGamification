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
import scala.util.matching.Regex
import scopt.Read
import scala.compiletime.ops.boolean

val stateDir = "/var/lib"
val r = scala.util.Random

// Recursive funtion for processing tasks
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
  val pattern= new Regex("name=[a-zA-z],description=[a-zA-Z],effort=[0-9],due=(\\d{4})-([01][0-9])-([012][0-9])")
  
  if((pattern.findAllIn(s).toList).length==1){
    // Args should look like "pg add name=hello,description=world,effort=12,due=2020-02-12"
    val args= parseTask(args =
        s.split(s",").toList.map(_.split("=").toList).flatten
      ) // Split on ',' first then split on '=' for head tail recursion. Flatten list of lists into single list
    Task(
      args("name").asInstanceOf[String],
      args("description").asInstanceOf[String],
      args("effort").asInstanceOf[Int],
      args("due").asInstanceOf[Date]
    )

  }
  
}

case class Config(
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
        .text("Manage tasks.")
        .children(
          // Entrypoint for args processing
          opt[Task]("add")
            .required()
            .action((x, c) => c.copy(task = Some(x)))
        )
    )

  }

  OParser.parse(parser, args, Config()) match
    // This is where our task object will end up as part of the config object
    case Some(config) => config
    case _            => println("Entered the wrong argument\n" +
      "Please Enter args like: pg add name=hello,description=world,effort=12,due=2020-02-12")
