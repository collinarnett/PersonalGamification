import com.fasterxml.jackson.module.scala.deser.overrides
case class Parameters(
    params: Map[String, String],
    tasks: Map[String, Task]
) {
  params.get("taskFile") match
    case None => {
      throw new IllegalArgumentException("--task-file is a required argument")
    }
    case Some(taskFile) => taskFile
}

sealed trait ArgumentType {
  def :+(other: String): ArgumentType =
    throw new java.lang.IllegalArgumentException
}
case class StringArgument(
    string: String
) extends ArgumentType 

case class BooleanArgument(
    bool: Boolean
) extends ArgumentType

case class SeqArgument(
    seq: Seq[String]
) extends ArgumentType {
  override def :+(other: String): ArgumentType =
    SeqArgument(this.seq :+ other)
}

object ArgumentParser {
  def parseArguments(
      args: Seq[String],
      params: Map[String, ArgumentType] = Map()
  ): Map[String, ArgumentType] =
    args match
      case "--task-file" :: taskFile :: tail =>
        parseArguments(
          tail,
          params ++ Map("taskFile" -> StringArgument(taskFile))
        )
      case "--player-file" :: playerFile :: tail =>
        parseArguments(
          tail,
          params ++ Map("playerFile" -> StringArgument(playerFile))
        )
      case "--complete" :: tail =>
        parseList(tail, params, "tasksToComplete")
      case Nil => params
      case _   => params

  def parseList(
      args: Seq[String],
      params: Map[String, ArgumentType] = Map(),
      index: String
  ): Map[String, ArgumentType] =
    args match
      case x :: tail if x.startsWith("--") =>
        parseArguments(args, params)
      case nextArgument :: tail =>
        parseList(
          tail,
          params.updated(
            index,
            params.getOrElse(index, SeqArgument(Seq())) :+ nextArgument
          ),
          index
        )
      case Nil => params
      case _   => params
}
