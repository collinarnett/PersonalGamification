import scopt.OParser
import scopt.Read
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.matching.Regex
import scala.compiletime.ops.string

// Recursive funtion for processing tasks
object Parser {

  def apply(args: Seq[String]) =
    OParser.parse(parser, args, Config()) match
      // This is where our task object will end up as part of the config object
      case Some(config) => config
      case _            => None

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
        parseTask(params ++ Map(s"effort" -> effort.toInt), tail)
      case s"due" :: due :: tail =>
        parseTask(
          params ++ Map(s"due" -> SimpleDateFormat("yyyy-mm-dd").parse(due)),
          tail
        )
      case Nil => params
      case _   => params

  private val builder = OParser.builder[Config]
  private val parser =
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
            .text("Example: pg task --add name=hello,description=world,effort=12,due=2020-02-12")   
        )
        
    )

}

implicit val taskRead: Read[Task] = Read.reads { (s: String) =>
  // Args should look like "pg add name=hello,description=world,effort=12,due=2020-02-12"
  val list: List[String] = List("world")
  // Split on ',' first then split on '=' for head tail recursion. Flatten list of lists into single list
  val args =
    Parser.parseTask(args =
      s.split(s",").toList.map(_.split("=").toList).flatten
    )
  // TODO Find a more scalable way to do argument validation, move this into scopt validation as well.
  
  Task(
    args("name").asInstanceOf[String],
    args("description").asInstanceOf[String],
    args("effort").asInstanceOf[Int],
    args("due").asInstanceOf[Date]
  )
}

case class Config(
    task: Option[Task] = None
)