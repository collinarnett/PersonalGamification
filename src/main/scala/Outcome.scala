import scala.util.Random
import scala.math._

case class Outcome(
    health: Int,
    exp: Float,
    items: Seq[Item],
    statusEffects: Seq[StatusEffect]
):
  override def toString(): String =
    s"""-----Outcome----- 
      |--Effect Heal and Exp--
      |Health: $health
      |Exp: $exp
      |--Items and Status Effects--
      |items: ${items.foreach(println(_))}
      |statusEffects: ${statusEffects.foreach(println(_))}
      ---------------------
      """.stripMargin

object Outcome:
  def reward(event: Event, effort: Int): Outcome =
    val positiveStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect > 0)
    val positiveItems: Seq[Item] =
      event.items.filter(_.affect > 0)
    Outcome(
      health = round(Random.nextFloat * effort * event.health),
      exp = Random.nextFloat * effort * event.exp,
      items = event.items.size match
        case x if x == 1 => event.items
        case x if x > 1 =>
          val outcome = choice(positiveItems, Random.between(1, x))
          outcome
        case _ => Seq()
      ,
      statusEffects = positiveStats.size match
        case x if x == 1 => positiveStats
        case x if x > 1 =>
          val outcome =
            choice(positiveStats, Random.between(1, x))
          outcome
        case _ => Seq()
    )

  def punishment(event: Event, effort: Int): Outcome =
    val negativeStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect < 0)
    Outcome(
      health = round(Random.nextFloat * effort * -event.health),
      exp = Random.nextFloat * -event.exp,
      items = Seq(),
      statusEffects =
        choice(negativeStats, Random.between(1, negativeStats.size))
    )

  private def choice[T](items: Seq[T], n: Int, acc: Seq[T] = Seq()): Seq[T] =
    Random.shuffle(items) match
      case x if n == 0 => acc
      case head +: tail =>
        choice(Random.shuffle(tail), n - 1, acc :+ head)
      case _ => acc
