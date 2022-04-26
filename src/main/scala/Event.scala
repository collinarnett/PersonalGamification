import scala.collection.IterableFactory

case class Event(
    monsters: Seq[Monster],
    items: Seq[Item],
    exp: Float,
    health: Int,
    statusEffects: Seq[StatusEffect]
):
    def string: String =
      s"""--- Effect Heal and Exp ---
      |health  : $health
      |exp : $exp
      |--Items, Monsters, and Status Effects
      |items : ${items.iterableFactory.iterate}
      |statusEffects  : ${statusEffects.iterableFactory.iterate}
      |monsters  :  ${monsters.iterableFactory.iterate}
      |""".stripMargin
