import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._

class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  val playerFile = File("player.yml")
  val taskFile = File("task.yml")

  "A Player" should "save a Player object" in {
    val player1 = Player()
    player1.save(playerFile)
    playerFile.exists should be(true)
  }

  it should "load a Player object" in {
    val player1 = Player()
    val player2 = player1.load(playerFile)
    (player2 == player1) should be(true)
  }

  // Delete testing file(s)
  playerFile.deleteOnExit()
  taskFile.deleteOnExit()
