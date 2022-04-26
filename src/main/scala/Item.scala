import java.util.Calendar
case class Item(
    name: String,
    description: String,
    affect: Int,
    lifetime: Calendar
):
    def string: String =
      s"""------ Item -------
      |name  : $name
      |description : $description
      |affect : $affect
      |lifetime  : $lifetime
      |""".stripMargin
