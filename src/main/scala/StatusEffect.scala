import java.util.Calendar
import java.text.SimpleDateFormat

case class StatusEffect(
    name: String,
    description: String,
    affect: Int,
    lifetime: Calendar
):
  override def toString(): String =
    s"""|Name : $name
        |Description : $description
        |Affect : $affect
        |LifeTime : ${SimpleDateFormat("MMMM-YYYY").format(lifetime.getTime())}
        """.stripMargin
