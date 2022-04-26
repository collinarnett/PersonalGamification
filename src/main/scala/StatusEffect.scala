import java.util.Calendar
case class StatusEffect(
    name: String,
    description: String,
    affect: Int,
    lifetime: Calendar
):
    def string: String =
      s"""------ StatusEffect -------
      |name  : $name
      |description : $description
      |affect : $affect
      |lifetime  : $lifetime
      |""".stripMargin
