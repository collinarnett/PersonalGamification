import org.scalatest.funsuite.AnyFunSuite
import java.io.File

class PersonalGamificationTests extends AnyFunSuite:

  val file = "test.yml"

  test("save should create a file") {
    val player1 = Player()
    player1.save(file)
    assert(File(file).exists)
  }

  test("load should load a Player object") {
    val player1 = Player()
    val player2 = player1.load(file)
    assert(player2 == player1)
  }

  // Delete testing file
  File(file).deleteOnExit()
