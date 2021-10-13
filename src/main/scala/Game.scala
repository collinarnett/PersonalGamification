import java.io.File
import Player._
def Game(player: Player, tasks: Seq[Task], complete: Seq[String]): Player =
  val tasksToComplete = tasks.diff(complete.collect { case name: String =>
    Task(name)
  })
  tasksToComplete match
    case Nil =>
      println("Complete: Tasks not found in task file.")
      player
    case _ =>
      println(s"Complete: $tasksToComplete")
      consumeTasks(
        tasksToComplete,
        player
      )
def consumeTasks(tasks: Seq[Task], player: Player): Player =
  tasks match
    case Nil          => player
    case task :: tail => consumeTasks(tail, player.completeTask(task))
