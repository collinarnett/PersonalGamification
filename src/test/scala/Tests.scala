import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._
import com.fasterxml.jackson.core.`type`.TypeReference

class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  val playerFile = File("player.yaml")
  val taskFile = File("tasks.yaml")

  "A Player" should "save a Player object" in {
    val player1 = Player()
    player1.save(playerFile)
    val hasBeenSaved = playerFile.exists
    playerFile.delete()
    hasBeenSaved should be(true)
  }

  it should "load a Player object" in {
    val player1 = Player()
    player1.save(playerFile)
    val player2 = Player()
    playerFile.delete()
    (player2 == player1) should be(true)
  }

  "Main" should "create initial player and tasks file" in {
    val args = "init"
    main(args)
    val haveBeenCreated = (playerFile.exists && taskFile.exists)
    playerFile.delete()
    taskFile.delete()
    haveBeenCreated should be(true)

  }

  // TODO refactor to use player test files
  it should "complete tasks" in {
    main(
      s"init --player-file ${playerFile.getAbsolutePath} --tasks-file ${taskFile.getAbsolutePath}"
    )
    val args = "play --complete task1"
    main(args)
    playerFile.delete()
    taskFile.delete()
  }
