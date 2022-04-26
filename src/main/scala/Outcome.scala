import scala.util.Random
import scala.math._
import scala.collection.IterableFactory

case class Outcome1(
    health: Int,
    exp: Float,
    items: Seq[Item],
    statusEffects: Seq[StatusEffect]
):
    def string: String =
      s"""--- Effect Heal and Exp ---
      |health  : $health
      |exp : $exp
      |--Items won and Char Stat Effects--
      |items : ${items.iterableFactory.iterate}
      |statusEffects  : ${statusEffects.iterableFactory.iterate}
      |""".stripMargin

//Changed the name to Outcome1 because their is already an object called
//Outcome in org.scalatest._
object Outcome1:
  def reward(event: Event, effort: Int): Outcome1 =
    val positiveStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect > 0)
    Outcome1(
      health = round(Random.nextFloat * effort * event.health),
      exp = Random.nextFloat * effort * event.exp,
      items = event.items.size match
        case x if x == 1 => event.items
        case x if x > 1 =>
          val outcome = choice(event.items, Random.between(1, x))
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

  def punishment(event: Event, effort: Int): Outcome1 =
    val negativeStats: Seq[StatusEffect] =
      event.statusEffects.filter(_.affect < 0)
    Outcome1(
      health = round(Random.nextFloat * effort * -event.health),
      exp = Random.nextFloat * -event.exp,
      items = Seq(),
      statusEffects =
        choice(negativeStats, Random.between(1, negativeStats.size))
    )

    // def apply(health: Int, exp: Float, items: Seq[Item], statusEffects: Seq[StatusEffect]): Outcome1 ={
    //   var o = new Outcome1(health=health, exp=exp, items=items, statusEffects=statusEffects)
    //   o
    // }

  private def choice[T](items: Seq[T], n: Int, acc: Seq[T] = Seq()): Seq[T] =
    Random.shuffle(items) match
      case x if n == 0 => acc
      case head +: tail =>
        choice(Random.shuffle(tail), n - 1, acc :+ head)
      case _ => acc
