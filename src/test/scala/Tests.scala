import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._
import com.fasterxml.jackson.core.`type`.TypeReference
import javax.lang.model.`type`.ErrorType
import java.util.Calendar
import java.util.GregorianCalendar

class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  it should "check to see if the add command is using the correct format" in {

    val strSeq: Seq[String] = Seq("add", "--name=hello", "--effort=10")
    val strSeq1: Seq[String] = Seq(
      "add",
      "name=hello, description=world, effort=12, due=2022-3-22"
    )
    val strSeq2: Seq[String] = Seq(
      "add",
      "--name=world",
      "--effort=12",
      "--description=hello my name is Justin",
      "--due=2022-3-22"
    )
    val strSeq3: Seq[String] =
      Seq("add", "aaaaaadkajkdjkajdkajdkajkdjkajdka111")
    val strSeq4: Seq[String] =
      Seq("add", "namehellodescriptionworldeffort122022-3-22")

    //Correct format since --description, and --due are optional
    Parser.apply(strSeq)
    
    //Error message should appear
    Parser.apply(strSeq1)

    //Correct format using --description and --due
    Parser.apply(strSeq2)

    //Error message should appear
    Parser.apply(strSeq3)

    //Error message should Appear
    Parser.apply(strSeq4)
  }

  it should "check to see if the delete command is using the correct format" in {

    val strSeq1: Seq[String] = Seq(
      "--delete",
      "--id=1"
    )

    val strSeq2: Seq[String] = Seq(
      "delete",
      "id=1"
    )

    val strSeq3: Seq[String] = Seq(
      "delete",
      "--id=-1"
    )

    val strSeq4: Seq[String] = Seq(
      "delete",
      "--id=1"
    )

    //Error message should appear since --delete is the wrong command, should just be delete
    Parser.apply(strSeq1)

    //Error message should appear since id=1 is the wrong command, should be --id=1
    Parser.apply(strSeq2)

    //Error message should appear since --id=-1 has an id of less than 0 when it should be >= 0
    Parser.apply(strSeq3)

    //Correct format
    Parser.apply(strSeq4)
  }

  it should "check to see if the list command is using the correct format" in {

    val strSeq1: Seq[String] = Seq(
      "--list"
    )

    val strSeq2: Seq[String] = Seq(
      "list"
    )

    //Error message because --list is the wront format, should be list
    Parser.apply(strSeq1)

    //Correct format
    Parser.apply(strSeq2)
  }

  it should "check to see if the modify command is using the correct format" in {

    val strSeq1: Seq[String] = Seq(
      "modify",
      "--id=1",
      "--name=world --effort=12 --description=hello my name is Justin --due=2022-3-22"
    )

    val strSeq2: Seq[String] = Seq(
      "modify",
      "--id=-1",
      "--name=world",
      "--effort=12",
      "--description=hello my name is Justin",
      "--due=2022-3-22"
    )

    val strSeq3: Seq[String] = Seq(
      "--modify",
      "--id=1",
      "--name=world",
      "--effort=12",
      "--description=hello my name is Justin",
      "--due=2022-3-22"
    )

    val strSeq4: Seq[String] = Seq(
      "modify",
      "--id=1",
    )

    val strSeq5: Seq[String] = Seq(
      "modify",
      "--id=1",
      "--name=world",
      "--effort=12",
      "--description=hello my name is Justin",
      "--due=2022-3-22"
    )

    //Error message should appear
    Parser.apply(strSeq1)

    //Error message since id is less than 0
    Parser.apply(strSeq2)

    //Error message since --modify is wrong format
    Parser.apply(strSeq3)

    //Correct format without the optional commands
    Parser.apply(strSeq4)

    //Correct format with optional commands
    Parser.apply(strSeq5)

  }

  it should "Accurately reward the player" in{

    val cal = Calendar.getInstance()

    val mon = new Monster(name="Mom", description="Your mom", toughness=69)
    val mons = Seq(mon)

    val itm = new Item(name="Butt", description="Hole", affect=6, lifetime=cal)
    val itms = Seq(itm)

    val status = new StatusEffect(name="Arm", description="Pit", affect=2, lifetime=cal)
    val statuses = Seq(status)

    val newEvent = new Event(monsters=mons, items=itms, exp=69.0, health=20, statusEffects=statuses)
    
    val newOutcome = Outcome1.apply(health=5, exp=14.0, items=itms, statusEffects=statuses)

    //Gives an error saying the reward is not a member of Outcome1
    //val finalOutcome = newOutcome.reward(newEvent, 4)
    
  }