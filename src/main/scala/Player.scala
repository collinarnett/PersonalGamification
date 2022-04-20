case class AbilityScores(
    strength: Float = 0,
    dexterity: Float = 0,
    constitution: Float = 0,
    intelligence: Float = 0,
    wisdom: Float = 0,
    charisma: Float = 0
) {

  def +(abilityScores: AbilityScores): AbilityScores =
    AbilityScores(
      strength + abilityScores.strength,
      dexterity + abilityScores.dexterity,
      constitution + abilityScores.constitution,
      intelligence + abilityScores.intelligence,
      wisdom + abilityScores.wisdom,
      charisma + abilityScores.charisma
    )
  def -(abilityScores: AbilityScores): AbilityScores =
    AbilityScores(
      strength - abilityScores.strength,
      dexterity - abilityScores.dexterity,
      constitution - abilityScores.constitution,
      intelligence - abilityScores.intelligence,
      wisdom - abilityScores.wisdom,
      charisma - abilityScores.charisma
    )

  def sum: Int =
    strength + dexterity + constitution + intelligence + wisdom + charisma

  def string =
    s"""strength    : $strength
    |dexterity   : $dexterity
    |constitution: $constitution
    |intelligence: $intelligence
    |wisdom      : $wisdom
    |charisma    : $charisma""".stripMargin
}

case class Player(
    name: String = "Player",
    exp: Float = 0.0,
    items: Seq[Items],
    statusEffects: Seq[StatusEffect],
    health: Int = 100,
    abilityScores: AbilityScores = AbilityScores()
):
  def ++(outcome: Outcome): Player =
    Player(
      name,
      exp + outcome.exp,
      items ++ outcome.items,
      statusEffects ++ outcome.statusEffects,
      health ++ outcome.health,
      abilityScores
    )

  def string: String =
    s"""------ Player -------
    |name   : $name
    |exp    : ${exp * 100}%
    |health : ($health/100)
    |--- Ability Scores --
    |${abilityScores.string}
    |""".stripMargin
