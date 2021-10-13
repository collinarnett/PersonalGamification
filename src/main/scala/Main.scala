import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.File
import scopt.OParser
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import com.fasterxml.jackson.core.`type`.TypeReference

val mapper = YAMLMapper.builder().addModule(DefaultScalaModule).build()

case class Config(
    status: Boolean = false,
    init: Boolean = false,
    playerFile: File = File("player.yaml"),
    tasksFile: File = File("tasks.yaml"),
    complete: Seq[String] = Seq()
)

def run(config: Config): Player =
  println("-------- Log ---------")
  val player: Player =
    if config.init then
      val blankPlayer = Player()
      if config.playerFile.exists then
        throw new FileAlreadyExistsException(config.playerFile.getPath)
      else
        blankPlayer.save(config.playerFile)
        blankPlayer
    else Player(config.playerFile)

  val tasks: Seq[Task] =
    if config.init then
      val blankTask = Task()
      if config.tasksFile.exists then
        throw new FileAlreadyExistsException(config.tasksFile.getPath)
      else
        mapper.writeValue(config.tasksFile, Seq(blankTask))
        Seq(blankTask)
    else mapper.readValue(config.tasksFile, new TypeReference[Seq[Task]]() {})

  if config.status then println(player)
  val complete = config.complete
  Game(player, tasks, complete)

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
            .validate(x =>
              try
                mapper.writeValue(x, classOf)
                x.delete()
                success
              catch
                case e: IOException =>
                  failure(s"Unable to create a file at ${x.getPath}:\n$e")
                case e: Exception => failure(s"Failed to create file:\n$e")
            )
            .valueName("<player-file>.yaml")
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml)."),
          opt[File]('t', "task-file")
            .optional()
            .validate(x =>
              try
                mapper.writeValue(x, classOf)
                x.delete()
                success
              catch
                case e: IOException =>
                  failure(s"Unable to create a file at ${x.getPath}:\n$e")
                case e: Exception => failure(s"Failed to create file:\n$e")
            )
            .valueName("<task-file>.yaml")
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml).")
        ),
      cmd("play")
        .children(
          opt[File]('p', "player-file")
            .valueName("<player-file>.yaml")
            .validate(x =>
              try
                if mapper
                    .readValue(x, classOf[Player])
                    .isInstanceOf[Player]
                then success
                else
                  failure(
                    s"Player cannot be loaded in file ${x.getPath}" +
                      s"\nPlease check the file's formatting."
                  )
              catch
                case e: IOException =>
                  failure(s"Unable to create player file at ${x.getPath}:\n$e")
                case e: Exception => failure(s"Failed to create file:\n$e")
            )
            .action((x, c) => c.copy(playerFile = x))
            .text("Location of your player file (eg. tasks.yaml)."),
          opt[File]('t', "tasks-file")
            .valueName("<taks-file>.yaml")
            .validate(x =>
              try
                if mapper
                    .readValue(x, classOf[Seq[Task]])
                    .isInstanceOf[Seq[Task]]
                then success
                else
                  failure(
                    s"Player cannot be loaded in file ${x.getPath}" +
                      s"\nPlease check the file's formatting."
                  )
              catch
                case e: IOException =>
                  failure(s"Unable to create tasks file at ${x.getPath}:\n$e")
                case e: Exception => failure(s"Failed to create file:\n$e")
            )
            .action((x, c) => c.copy(tasksFile = x))
            .text("Location of your tasks file (eg. tasks.yaml)."),
          opt[Seq[String]]('c', "complete")
            .valueName("task1, task2...")
            .minOccurs(1)
            .action((x, c) => c.copy(complete = x))
            .text("Tasks to complete.")
        ),
      help("help")
    )

  }
  val player = OParser.parse(parser, args, Config()) match
    case Some(config) => run(config)
    case _            =>
  player match
    case p: Player => p.save(p.file)
  println("---------------------")
  println(player)
  println("See you tomorrow.\n")
