@main def main(args: String*) =
  val params = ArgumentParser.parseArguments(args)
  val taskFile = params("taskFile")
  val playerFile = params("playerFile")
  val player = Player(playerFile.toString)
  val task = Task(taskFile.toString)
