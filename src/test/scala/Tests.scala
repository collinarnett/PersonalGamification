import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._
import com.fasterxml.jackson.core.`type`.TypeReference


class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  it should "tell whether args are formatted correctly" in {
    val args="pg task add name=hello,description=world,effort=1,due=2022-02-22"
    assert(validArg(args)==true)
    // val args1="pg add"
    // assert(validArg(args1)==false)
    // val args2="pg add 122313i3i1o313io13i"
    // assert(validArg(args2)==false)
    // val args3="pg add nami=hello, description=world, effort=12, due=2022-3-22"
    // assert(validArg(args3)==false)
    // val args4=  "pg add namehellodescriptionworldeffort122022-3-22"
    // assert(validArg(args4)==false)
    // val args5= "pg add nami=hello, description=world, effort=12, due=hello"
    // assert(validArg(args5)==false)     
  }
  it should "take in args and read the taskfrom it" in {
    val arg="pg task"
    //Oparser.parse(taskRead, arg, config())    
    main()
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
