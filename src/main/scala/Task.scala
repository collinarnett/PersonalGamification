import java.util.Calendar

case class Task(name: String, effort: Int, description: String, due: Calendar):
  def update(config: Config): Task =
    Task(
      config.name.getOrElse(this.name),
      config.effort.getOrElse(this.effort),
      config.description.getOrElse(this.description),
      config.due.getOrElse(this.due)
    )
  override def toString(): String =
    s"""-------Task--------
    |Name   :  $name
    |Effort :   $effort
    |Description :  $description
    |Due    :  $due
    ------------------------
    """.stripMargin
