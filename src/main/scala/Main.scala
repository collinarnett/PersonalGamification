val stateDir = "/var/lib"
val r = scala.util.Random

///case class Person(name:String, email:String, age:Int)

@main def main(args: String*) =
  val config = Parser(args)
  //val person=new Person("Ab", "@gmail.com", 12)
  //writer.seralize("hello.txt", person)