case class Event(
    monsters: Seq[Monster],
    items: Seq[Item],
    experience: Float,
    health: Int,
    statusEffects: Seq[StatusEffect]
)
