import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._
import com.fasterxml.jackson.core.`type`.TypeReference



class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  it should "take Properly format args and create a task object" in {

    val strSeq: Seq[String]=Seq("task", "--add")
    val strSeq1:Seq[String]=Seq("task", "--add", "name=hello, description=world, effort=12, due=2022-3-22")
    val strSeq2:Seq[String]=Seq("task", "--add", "name=42.35,description=-1000,effort=12,due=2022-3-22")
    val strSeq3:Seq[String]=Seq("task", "--add", "aaaaaadkajkdjkajdkajdkajkdjkajdka111")
    val strSeq4:Seq[String]=Seq("task", "--add",  "namehellodescriptionworldeffort122022-3-22")

      Parser.apply(strSeq)
      Parser.apply(strSeq1)
      Parser.apply(strSeq2)
      Parser.apply(strSeq3)
      Parser.apply(strSeq4) 
    
  }
    

   
  // val playerFile = File("player.yaml")
  // val taskFile = File("tasks.yaml")
  
  // "A Player" should "save a Player object" in {
  //   val player1 = Player()
  //   player1.save(playerFile)
  //   val hasBeenSaved = playerFile.exists
  //   playerFile.delete()
  //   hasBeenSaved should be(true)
  // }
  // it should "load a Player object" in {
  //   val player1 = Player()
  //   player1.save(playerFile)
  //   val player2 = Player()
  //   playerFile.delete()
  //   (player2 == player1) should be(true)
  // }

  // "Main" should "create initial player and tasks file" in {
  //   val args = "init"
  //   main(args)
  //   val haveBeenCreated = (playerFile.exists && taskFile.exists)
  //   playerFile.delete()
  //   taskFile.delete()
  //   haveBeenCreated should be(true)

  // }
