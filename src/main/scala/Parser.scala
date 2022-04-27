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
      config.name match
        case Some(name) => name
        case None       => ""
      ,
      config.effort match
        case Some(effort) => effort
        case None         => 0
      ,
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
        "0.0.1",
        "https://github.com/collinarnett/PersonalGamification"
      ),
      cmd("add")
        .action((x, c) => c.copy(mode = "task add"))
        .text("Add a task")
        .children(
          opt[String]('n', "name")
            .required()
            .valueName("<str>")
            .action((x, c) => c.copy(name = Some(x)))
            .text("Name of the task"),
          opt[Int]('e', "effort")
            .required()
            .valueName("<int>")
            .action((x, c) => c.copy(effort = Some(x)))
            .text("Amount of effort required to complete the task, eg. 2"),
          opt[String]('d', "description")
            .optional()
            .valueName("<str>")
            .action((x, c) => c.copy(description = Some(x)))
            .text("Description of the task, eg. \"Wash the dishes\""),
          opt[Calendar]('D', "due")
            .optional()
            .valueName("YYYY-MM-DD")
            .action((x, c) => c.copy(due = Some(x)))
            .text("When the task is due, eg. 2022-12-31")
        ),
      cmd("delete")
        .action((x, c) => c.copy(mode = "task delete"))
        .text("Delete a task")
        .children(
          opt[Int]('i', "id")
            .required()
            .valueName("<int>")
            .text("Id of the task to delete. Use \"list\" to display IDs")
            .action((x, c) => c.copy(id = x))
            .validate(x =>
              if (x >= 0) success
              else failure("Argument id must be >0")
            )
        ),
      cmd("list")
        .action((x, c) => c.copy(mode = "task list"))
        .text("List all tasks"),
      cmd("modify")
        .action((x, c) => c.copy(mode = "task modify"))
        .text("Modify a task")
        .children(
          opt[Int]('i', "id")
            .required()
            .valueName("<int>")
            .text("Id of the task to modify. Use \"list\" to display IDs")
            .action((x, c) => c.copy(id = x))
            .validate(x =>
              if (x >= 0) success
              else failure("Argument id must be >0")
            ),
          opt[String]('n', "name")
            .optional()
            .valueName("<str>")
            .action((x, c) => c.copy(name = Some(x)))
            .text("Name of the task"),
          opt[Int]('e', "effort")
            .optional()
            .valueName("<int>")
            .action((x, c) => c.copy(effort = Some(x)))
            .text("Amount of effort required to complete the task, eg. 2"),
          opt[String]('d', "description")
            .optional()
            .valueName("<str>")
            .action((x, c) => c.copy(description = Some(x)))
            .text("Description of the task, eg. \"Wash the dishes\""),
          opt[Calendar]('D', "due")
            .optional()
            .valueName("YYYY-MM-DD")
            .action((x, c) => c.copy(due = Some(x)))
            .text("When the task is due, eg 2022-12-31")
        ),
      cmd("complete")
        .action((x, c) => c.copy(mode = "task complete"))
        .text("Complete a task")
        .children(
          opt[Int]('i', "id")
            .required()
            .valueName("<int>")
            .text("Id of the task to complete. Use \"list\" to display IDs")
            .action((x, c) => c.copy(id = x))
            .validate(x =>
              if (x >= 0) success
              else failure("Argument id must be >0")
            )
        ),
      cmd("player")
        .action((x, c) => c.copy(mode = "player add"))
        .text("Add a player")
        .children(
          opt[String]('n', "name")
            .required()
            .valueName("<str>")
            .action((x, c) => c.copy(player = x))
            .text("The name of your player")
        ),
      help("help").text("prints this usage text")
    )

case class Config(
    mode: String = "None",
    name: Option[String] = None,
    effort: Option[Int] = None,
    description: Option[String] = None,
    due: Option[Calendar] = None,
    id: Int = -1,
    player: String = "Player"
)
