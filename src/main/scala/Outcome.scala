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
    Outcome(
      health = round(Random.nextFloat * effort * event.health),
      exp = Random.nextFloat * effort * event.exp,
      items = choice(event.items, Random.nextInt(event.items.size)),
      statusEffects =
        choice(event.statusEffects, Random.nextInt(event.statusEffects.size))
    )
  def punishment(event: Event): Outcome =
    Outcome(
      health = round(Random.nextFloat * effort * -event.health),
      exp = Random.nextFloat * -event.exp,
      items = Seq(),
      statusEffects = ???
    )

  private def choice(items: Seq[Any], n: Int): Seq[Any] =
    for (_ <- 0 until n) yield Random.shuffle(items).head
