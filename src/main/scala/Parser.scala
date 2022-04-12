import scopt.OParser
import java.util.GregorianCalendar
import java.util.Calendar

// Recursive funtion for processing tasks
object Parser:
  def apply(args: Seq[String]): Config =
    OParser.parse(parser, args, Config()) match
      // This is where our task object will end up as part of the config object
      case Some(config) => config
      case None         => Config()

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
      cmd("add")
        .action((x, c) => c.copy(mode = "task add"))
        .text("Add a task")
        .children(
          arg[String]("name")
            .required()
            .action((x, c) => c.copy(name = x))
            .text("Name of the task"),
          arg[Int]("effort")
            .required()
            .action((x, c) => c.copy(effort = x))
            .text("Amount of effort required to complete the task"),
          arg[String]("description")
            .action((x, c) => c.copy(description = Some(x)))
            .text("Description of the task"),
          arg[Calendar]("due")
            .action((x, c) => c.copy(due = Some(x)))
            .text("When the task is due.")
        ),
      cmd("delete")
        .action((x, c) => c.copy(mode = "task delete"))
        .text("Delete a task")
        .children(
          arg[Int]("id")
            .text("Id of the task to delete")
            .action((x, c) => c.copy(id = x))
        ),
      cmd("list")
        .action((x, c) => c.copy(mode = "task list"))
        .text("List all tasks")
    )

case class Config(
    mode: String = "None",
    name: String = "None",
    effort: Int = 0,
    description: Option[String] = None,
    due: Option[Calendar] = None,
    id: Int = -1
)
