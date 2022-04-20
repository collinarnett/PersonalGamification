import scala.util.Random
import scala.math._

case class Outcome(
    health: Int,
    exp: Float,
    items: Seq[Items],
    statusEffects: Seq[StatusEffect]
)

object Outcome:
  def reward(event: Event, effort: Int): Outcome =
    val positiveStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect > 0)
    Outcome(
      health = round(Random.nextFloat * effort * event.health),
      exp = Random.nextFloat * effort * event.exp,
      items = choice(event.items, Random.nextInt(event.items.size)),
      statusEffects = choice(positiveStats, Random.nextInt(positiveStats.size))
    )
  def punishment(event: Event, effort: Int): Outcome =
    val negativeStats: Seq[StatusEffect] =
      event.statuseffects.filter(_.affect < 0)
    Outcome(
      health = round(Random.nextFloat * effort * -event.health),
      exp = Random.nextFloat * -event.exp,
      items = Seq(),
      statusEffects = choice(negativeStats, Random.nextInt(negativeStats.size))
    )

  private def choice(items: Seq[Any], n: Int, acc: Seq[Any] = Seq()): Seq[Any] =
    items match
      case head :: tail => choice(Random.shuffle(tail), n - 1, acc ++ head)
      case n == 0       => acc
      case _            => acc
