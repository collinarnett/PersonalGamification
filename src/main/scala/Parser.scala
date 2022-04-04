import scopt.OParser
import java.util.GregorianCalendar
import java.util.Calendar

// Recursive funtion for processing tasks
object Parser {
  def apply(args: Seq[String]) =
    OParser.parse(parser, args, Config()) match
      // This is where our task object will end up as part of the config object
      case Some(config) => mode(config)
      case _            => None

  def parseTask(config: Config): Task =
    Task(
      config.name,
      config.effort,
      config.description match
        case Some(value) => value
        case None        => ""
      ,
      config.due match
        case Some(value) => value
        case None        => GregorianCalendar(9999, 0, 0)
    )

  private def mode(config: Config) =
    config.mode match
      case "add" => parseTask(config)
      case _     =>

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
          cmd("add")
            .required()
            .action((x, c) => c.copy(mode = "add"))
            .text("Add a task")
            .children(
              opt[String]("name")
                .required()
                .action((x, c) => c.copy(name = x))
                .text("Name of the task"),
              opt[Int]("effort")
                .required()
                .action((x, c) => c.copy(effort = x))
                .text("Amount of effort required to complete the task"),
              opt[String]("description")
                .action((x, c) => c.copy(description = Some(x)))
                .text("Description of the task"),
              opt[Calendar]("due")
                .action((x, c) => c.copy(due = Some(x)))
                .text("When the task is due.")
            )
        )
    )

}

case class Config(
    mode: String = "None",
    name: String = "None",
    effort: Int = 0,
    description: Option[String] = None,
    due: Option[Calendar] = None
)
