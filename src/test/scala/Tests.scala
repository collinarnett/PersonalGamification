import org.scalatest._
import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import matchers._
import com.fasterxml.jackson.core.`type`.TypeReference

class PersonalGamificationSpec extends AnyFlatSpec with should.Matchers:

  it should "take Properly format args and create a task object" in {

    val strSeq: Seq[String] = Seq("task", "--add")
    val strSeq1: Seq[String] = Seq(
      "task",
      "--add",
      "name=hello, description=world, effort=12, due=2022-3-22"
    )
    val strSeq2: Seq[String] = Seq(
      "task",
      "add",
      "--name world",
      " --effort 12",
      " --description hello my name is Abenezer",
      " --due 2022-3-22"
    )
    val strSeq3: Seq[String] =
      Seq("task", "--add", "aaaaaadkajkdjkajdkajdkajkdjkajdka111")
    val strSeq4: Seq[String] =
      Seq("task", "--add", "namehellodescriptionworldeffort122022-3-22")

    Parser.apply(strSeq2)
  }
  it should "take in an object and seralize it" in {
    case class Person(name:String, email:String, age: Int)
    val nP = new Person("Abenezer1", "@gmail.com", 12)
    val path = os.pwd
    writer.seralize(path, "hello.txt", nP)
  }



    
