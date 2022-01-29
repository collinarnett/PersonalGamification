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
    tasksToDo: Seq[File] = Seq(),
    done: Seq[String] = Seq()
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
        ),
      opt[Seq[File]]("add")
        .action((x, c) => c.copy(tasksToDo = x)),
      opt[Seq[String]]("done")
        .action((x, c) => c.copy(done = x))
      )
  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => 
    case _            =>
