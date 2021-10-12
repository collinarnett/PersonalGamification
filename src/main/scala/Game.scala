import java.io.File
import Player._
def Game(player: Player, tasks: Seq[Task], completed: Seq[String]): Player =
  completed match
    case Nil => player
    case _ =>
      println(s"Completed:\n$completed")
      consumeTasks(
        tasks.diff(for (taskName <- completed) yield Task(taskName)),
        player
      )
def consumeTasks(tasks: Seq[Task], player: Player): Player =
  tasks match
    case Nil          => player
    case task :: tail => consumeTasks(tail, player.completeTask(task))
