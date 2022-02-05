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
    list: Boolean = false,
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
      opt[Unit]("status")
        .action((_, c) => c.copy(status = true))
        .text("Display your player status."),
      opt[File]("player")
        .action((x, c) => c.copy(playerFile = x)),
      opt[Seq[File]]("add")
        .action((x, c) => c.copy(tasksToDo = x)),
      opt[Unit]("list")
        .action((_, c) => c.copy(list = true)),
      opt[Seq[String]]("done")
        .action((x, c) => c.copy(done = x))
      )
  }

  OParser.parse(parser, args, Config()) match
    case Some(config) => parseFile(config)
    case _ => None


private def parseFile(config: Config): Option[Player] = 
  val mapper = new YAMLMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  Try { mapper.readValue[Player](config.playerFile) } match
      case Success(p) => Some(p)
      case Failure(e) =>
        println(s"Failed to load player file. Reason: $e")
        None

private def parseTasks(config: Config): Seq[Option[Task]] =
  val mapper = new YAMLMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  for ((task: File) <- config.tasksToDo) yield
    Try { mapper.readValue[Task](task) } match
        case Success(t) => Some(t)
        case Failure(e) =>
          println(s"Failed to load task file. Reason: $e")
          None 

