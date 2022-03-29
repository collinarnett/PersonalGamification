val stateDir = "/var/lib"
val r = scala.util.Random

@main def main(args: String*) =
  val config = Parser(args)