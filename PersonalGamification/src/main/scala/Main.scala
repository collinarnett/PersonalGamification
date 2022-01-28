import java.io.File
import scopt.OParser
import scala.util.Success
import scala.util.Failure
import scala.util.Try
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
def afterNow(dd: String): Boolean =
  LocalDateTime.now().isAfter(LocalDateTime.parse(dd, formatter))
val r = scala.util.Random

case class Config(
    status: Boolean = false,
    init: Boolean = false,
    playerFile: File = File("player.yaml"),
    tasksFile: File = File("tasks.yaml"),
    complete: Seq[String] = Seq()
)

def run(config: Config) =

  val mapper = new YAMLMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  val quest: Quest = Try { mapper.readValue[Quest](config.tasksFile) } match
    case Success(q) => q
    case Failure(e) =>
      println(s"Failed. Reason: $e")
      Quest()

  val player: Player = Try { mapper.readValue[Player](config.playerFile) } match
    case Success(p) => p
    case Failure(e) =>
      println(s"Failed. Reason: $e")
      Player()

  if config.status then println(player.string)
  else if config.init then
    quest.save(config.tasksFile)
    player.save(config.playerFile)
  else
    val completed =
      Quest(for
        task <- quest.tasks
        taskName <- config.complete
        if (task.name == taskName)
      yield task)
    val unfinished =
      Quest(for
        task <- quest.tasks
        taskName <- config.complete
        if (task.name != taskName) && !afterNow(task.dueDate)
      yield task)

    println(completed)
    val updatedPlayer = player.completeQuest(completed.exp)
    println(updatedPlayer.string)
    println("Would you like to save your progress?")
    if UI.yesNoPrompt() then
      updatedPlayer.save(config.playerFile)
      unfinished.tasks match
        case Seq() => Quest().save(config.tasksFile)
        case _     => unfinished.save(config.tasksFile)

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
      opt[Unit]('s', "status")
        .action((_, c) => c.copy(status = true))
        .text("Display your player status."),
      cmd("init")
        .text("Initialize a new blank character and task sheet.")
        .action((_, c) => c.copy(init = true))
        .children(
          opt[File]('p', "player-file")
            .optional()
            .valueName("<player-file>.yaml")
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml)."),
          opt[File]('t', "task-file")
            .valueName("<task-file>.yaml")
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml).")
        ),
      cmd("play")
        .children(
          opt[File]('p', "player-file")
            .valueName("<player-file>.yaml")
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml)."),
          opt[File]('t', "tasks-file")
            .valueName("<taks-file>.yaml")
            .action((x, c) => c.copy(tasksFile = x))
            .text("Location of your tasks file (eg. tasks.yaml)."),
          opt[Seq[String]]('c', "complete")
            .optional()
            .valueName("task1, task2...")
            .action((x, c) => c.copy(complete = x))
            .text("Tasks to complete.")
        ),
      help("help")
    )

  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => run(config)
    case _            =>
