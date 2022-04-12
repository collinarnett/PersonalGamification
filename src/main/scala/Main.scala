import java.util.UUID.randomUUID
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.ClassTagExtensions

val stateDir = os.Path("/var/lib/pg")

@main def main(args: String*) =
  val config: Config = Parser(args)
  val mapper =
    new YAMLMapper() with ClassTagExtensions
  println(s"$stateDir - is the current directory")
  println(os.list(stateDir))
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
      val task = Parser.parseTask(config)
      val fileToModify: os.Path = os
        .list(stateDir)
        .filter(_.baseName.endsWith("incomplete"))
        .map(path => (mapper.readValue[Task](path.toIO) -> path))
        .filter((k, v) => k == task)
        .map((k, v) => v)
        .head
      mapper.writeValue(fileToModify.toIO, task)
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
