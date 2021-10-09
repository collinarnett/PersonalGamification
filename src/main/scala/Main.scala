import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.File
@main def main(args: String*) =
  val mapper = YAMLMapper.builder().addModule(DefaultScalaModule).build()

  val params = ArgumentParser.parseArguments(args)
  val taskFile = params("taskFile")
  val playerFile = params("playerFile")
  val player = Player(playerFile.toString)
  val tasks = mapper.readValue(new File(taskFile.toString), classOf[Seq[Task]])
  def consumeTasks(list: Seq[Task], player: Player): Player = list match {
    case task :: tail => consumeTasks(tail, player.completeTask(task))
    case Nil          => player

  }
  consumeTasks(tasks, player)
