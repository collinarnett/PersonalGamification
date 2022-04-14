import java.util.UUID.randomUUID
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions

val stateDir = os.Path("/var/lib/pg")

@main def main(args: String*) =
  val config: Config = Parser(args)
  val mapper =
    new YAMLMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  config.mode match
    case "task add" =>
      val task = Parser.parseTask(config)
      val fileName = s"${randomUUID()}_incomplete.yaml"
      mapper.writeValue((stateDir / fileName).toIO, task)
    case "task delete" =>
      val id = config.id
      val fileToDelete =
        os.list(stateDir).filter(_.baseName.endsWith("incomplete"))(id)
      os.move(
        fileToDelete,
        stateDir / fileToDelete.baseName.replace("incomplete", "deleted")
      )
    case "task modify" =>
      val id = config.id
      val fileToModify: os.Path = os
        .list(stateDir)
        .filter(_.baseName.endsWith("incomplete"))(id)
      val oldTask: Task = mapper.readValue[Task](fileToModify.toIO)
      mapper.writeValue(fileToModify.toIO, oldTask.update(config))
    case "task list" =>
      os.list(stateDir)
        .filter(_.baseName.endsWith("incomplete"))
        .zipWithIndex
        .foreach((path, index) =>
          println(s"$index - ${mapper.readValue[Task](path.toIO).name}")
        )
    case "task complete" =>
      val id = config.id
      val fileToComplete =
        os.list(stateDir).filter(_.baseName.endsWith("incomplete"))(id)
      os.move(
        fileToComplete,
        stateDir / fileToComplete.baseName.replace("incomplete", "complete")
      )
    case _ =>
