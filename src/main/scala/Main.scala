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
  mapper.registerModule(DefaultScalaModule)
  val r = scala.util.Random

  // Try to load assets
  val monsters: Seq[Monster] =
    os.makeDir.all(stateDir / "monsters")
    os.list(stateDir / "monsters")
      .map(m => mapper.readValue[Monster](m.toIO))
  val items: Seq[Item] =
    os.makeDir.all(stateDir / "items")
    os.list(stateDir / "items")
      .map(i => mapper.readValue[Item](i.toIO))
  val statusEffects: Seq[StatusEffect] =
    os.makeDir.all(stateDir / "status_effects")
    os.list(stateDir / "status_effects")
      .map(s => mapper.readValue[StatusEffect](s.toIO))

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
            stateDir / file.baseName.replace("incomplete", "deleted.yaml")
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
      // Task we want to complete
      val id = config.id
      // Load Player
      val playerFile: os.Path =
        os.list(stateDir)
          .filter(_.baseName.endsWith("player"))
          .lift(0)
          .getOrElse(
            {
              System.err.println("Error - player file not found")
              System.exit(1).asInstanceOf[Nothing]
            }
          )
      val player = mapper.readValue[Player](
        playerFile.toIO
      )
      // TODO Refactor `asInstanceOf` into option handling
      val fileToComplete: os.Path =
        os.list(stateDir)
          .filter(_.baseName.endsWith("incomplete"))
          .lift(id)
          .getOrElse(
            {
              System.err.println("Error - player file not found")
              System.exit(1).asInstanceOf[Nothing]
            }
          )
      val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          monsters,
          items,
          r.nextFloat,
          r.nextInt(10),
          statusEffects
        )
        // Load the task
        val task: Task = mapper.readValue[Task](fileToComplete.toIO)
        // Check if task has expired
        val timeDiff: Long =
          task.due.getTimeInMillis - Calendar.getInstance.getTimeInMillis
        timeDiff match
          case x if x <= 0 =>
            // Punish if expired
            player ++ Outcome.punishment(event, task.effort)
          case x if x > 0 =>
            // Reward if not expired
            player ++ Outcome.reward(event, task.effort)
      mapper.writeValue(playerFile.toIO, updatedPlayer)
      os.move(
        fileToComplete,
        stateDir / fileToComplete.baseName.replace("incomplete", "complete")
      )

    case _ => System.exit(0)
  System.exit(0)
