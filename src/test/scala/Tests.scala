import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._

class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  val playerFile = "player.yml"
  val taskFile = "task.yml"

  "A Player" should "save a Player object" in {
    val player1 = Player()
    player1.save(playerFile)
    File(playerFile).exists should be(true)
  }

  it should "load a Player object" in {
    val player1 = Player()
    val player2 = player1.load(playerFile)
    (player2 == player1) should be(true)
  }

  "ArgumentParser" should "load commandline arguments" in {
    val args = Seq("--task-file", taskFile, "--player-file", playerFile)
    val params = ArgumentParser.parseArguments(args)
    val correctParams = Map(
      "taskFile" -> StringArgument(taskFile),
      "playerFile" -> StringArgument(playerFile)
    )
    (params == correctParams) should be(true)
  }

  // Delete testing file(s)
  File(playerFile).deleteOnExit()
  File(taskFile).deleteOnExit()
