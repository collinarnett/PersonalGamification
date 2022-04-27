import java.util.Calendar
import java.text.SimpleDateFormat

case class Item(
    name: String,
    description: String,
    affect: Int,
    lifetime: Calendar
):
  override def toString(): String =
    s"""| Name : $name
        | Description: $description
        | Affect: $affect
        | Lifetime : ${SimpleDateFormat("MMMM-YYYY").format(lifetime.getTime())}
        |-------------------------
        """.stripMargin
