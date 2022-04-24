import scala.util.Random
import scala.math._

case class Outcome(
    health: Int,
    exp: Float,
    items: Seq[Item],
    statusEffects: Seq[StatusEffect]
)

object Outcome:
  def reward(event: Event, effort: Int): Outcome =
    val positiveStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect > 0)
    Outcome(
      health = round(Random.nextFloat * effort * event.health),
      exp = Random.nextFloat * effort * event.exp,
      items = event.items.size match
        case x if x > 0 =>
          choice(event.items, Random.nextInt(x))
        case _ => Seq()
      ,
      statusEffects = positiveStats.size match
        case x if x > 0 =>
          choice(positiveStats, Random.nextInt(positiveStats.size))
        case _ => Seq()
    )

  def punishment(event: Event, effort: Int): Outcome =
    val negativeStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect < 0)
    Outcome(
      health = round(Random.nextFloat * effort * -event.health),
      exp = Random.nextFloat * -event.exp,
      items = Seq(),
      statusEffects = choice(negativeStats, Random.nextInt(negativeStats.size))
    )

  private def choice[T](items: Seq[T], n: Int, acc: Seq[T] = Seq()): Seq[T] =
    items match
      case head :: tail => choice(Random.shuffle(tail), n - 1, acc :+ head)
      case x if n == 0  => acc
      case _            => acc
