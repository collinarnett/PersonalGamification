case class Event(
    monsters: Seq[Monster],
    items: Seq[Item],
    exp: Float,
    health: Int,
    statusEffects: Seq[StatusEffect]
)
