import java.util.UUID.randomUUID
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions
import java.util.{Locale, Calendar, GregorianCalendar}
import java.text.SimpleDateFormat
val stateDir = os.Path("/var/lib/pg")

@main def main(args: String*) =
  val config: Config = Parser(args)
  val mapper =
    new YAMLMapper() with ClassTagExtensions

  // Try to load assets
  val monsters: Seq[Monster] = os.list((stateDir / "monsters").mkdir.all)
  val items: Seq[Item] =
    os.list(
      (stateDir / "items").mkdir.all
    )
  val statusEffects: Seq[StatusEffect] =
    os.list(
      (stateDir / "status_effects").mkdir.all
    )

  mapper.registerModule(DefaultScalaModule)
  config.mode match
    case "player add" =>
      val player = Player(name = config.player)
      val fileName = s"${randomUUID()}_player.yaml"
      mapper.writeValue((stateDir / fileName).toIO, player)
    case "task add" =>
      val task = Parser.parseTask(config)
      val fileName = s"${randomUUID()}_incomplete.yaml"
      mapper.writeValue((stateDir / fileName).toIO, task)
    case "task delete" =>
      val id = config.id
      val fileToDelete: Option[os.Path] =
        os.list(stateDir).filter(_.baseName.endsWith("incomplete")).lift(id)
      fileToDelete match
        case Some(file) =>
          os.move(
            file,
            stateDir / file.baseName.replace("incomplete", "deleted")
          )
        case None =>
          System.err.println("Error - Task not in list")
          System.exit(1)
    case "task modify" =>
      val id = config.id
      val fileToModify: Option[os.Path] = os
        .list(stateDir)
        .filter(_.baseName.endsWith("incomplete"))
        .lift(id)
      fileToModify match
        case Some(file) =>
          val oldTask: Task = mapper.readValue[Task](file.toIO)
          mapper.writeValue(
            file.toIO,
            oldTask.update(config)
          )
        case None =>
          System.err.println("Error - Task not in list")
          System.exit(1)
    case "task list" =>
      os.list(stateDir)
        .filter(_.baseName.endsWith("incomplete"))
        .zipWithIndex
        .foreach((path, index) =>
          println(s"$index - ${mapper.readValue[Task](path.toIO).name}")
        )
    case "task complete" =>
      val id = config.id
      val player: Player =
        mapper.readValue[Player](
          os.list(stateDir)
            .filter(_.baseName.endsWith("player"))
            .lift(0)
            .getOrElse(
              {
                System.err.println("Error - player file not found")
                System.exit(1)
              }
            )
            .toIO
        )

      val fileToComplete: Option[os.Path] =
        os.list(stateDir)
          .filter(_.baseName.endsWith("incomplete"))
          .lift(id)
      fileToComplete match
        case Some(file) =>
          // Trigger Event
          val task: Task = mapper.readValue[Task](file.toIO)
          val timeDiff: Long =
            task.due.getTimeInMillis - Calendar.getInstance.getTimeInMillis
          val outcome =
            timeDiff match
              case _ <= 0 =>
                player + Outcome.punishment(???)
              case _ > 0 =>
                player + Outcome.reward(???)
          mapper.writeValue(
            player
          )
          os.move(
            file,
            stateDir / file.baseName.replace("incomplete", "complete")
          )
        case None =>
          System.err.println("Error - Task not in list")
          System.exit(1)
    case _ => System.exit(0)
  System.exit(0)
