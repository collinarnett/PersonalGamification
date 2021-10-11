import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.File
import scopt.OParser
import java.io.FileNotFoundException
import java.io.IOException

@main def main(args: String*) =
  val mapper = YAMLMapper.builder().addModule(DefaultScalaModule).build()

  case class Config(
      status: Boolean = false,
      player: File = File("player.yaml"),
      tasks: File = File("tasks.yaml"),
      completed: Seq[String] = Seq("")
  )

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
      opt[Unit]('s', "status")
        .action((_, c) => c.copy(status = true))
        .text("Display your player status."),
      opt[File]('p', "player")
        .required()
        .valueName("<file>.yaml")
        .action((x, c) => c.copy(player = x))
        .text("Location of your player file (eg. player.yaml)."),
      opt[File]('t', "tasks")
        .required()
        .valueName("<file>.yaml")
        .action((x, c) => c.copy(tasks = x))
        .text("Location of your tasks file (eg. tasks.yaml)."),
      opt[Seq[String]]('c', "completed")
        .valueName("task1, task2...")
        .action((x, c) => c.copy(completed = x))
        .text("Tasks to be marked as completed."),
      help("help")
    )

  }
  // OParser.parse returns Option[Config]
  OParser.parse(parser, args, Config()) match {
    case Some(config) =>
      try
        val player = Player(config.player)
        val tasks = mapper.readValue(config.tasks, classOf[Seq[Task]])
        val completed = config.completed
        consumeTasks(
          tasks.diff(for (taskName <- completed) yield Task(taskName)),
          player
        )
      catch
        case e: FileNotFoundException => println("File not found.")
        case e: IOException           => println("IO error.")

    case _ =>
    // arguments are bad, error message will have been displayed
  }

  def consumeTasks(tasks: Seq[Task], player: Player): Player =
    tasks match {
      case Nil          => player
      case task :: tail => consumeTasks(tail, player.completeTask(task))
    }
