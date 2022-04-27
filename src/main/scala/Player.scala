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

  def sum: Float =
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
    name: String,
    exp: Float = 0.0,
    items: Seq[Item] = Seq(),
    statusEffects: Seq[StatusEffect] = Seq(),
    health: Int = 100,
    abilityScores: AbilityScores = AbilityScores()
):
  def ++(outcome: Outcome): Player =
    Player(
      name,
      exp + outcome.exp,
      (items ++ outcome.items).distinct,
      (statusEffects ++ outcome.statusEffects).distinct,
      health + outcome.health,
      abilityScores
    )

  override def toString: String =
    s"""------ Player -------
    |name   : $name
    |exp    : ${exp * 10}%
    |health : $health
    |--- Ability Scores --
    |${abilityScores.string}
    |------ Items --------
    |${items.foldLeft("")(_ + "\n" + _)}
    |--- Status Effects --
    |${statusEffects.foldLeft("")(_ + "\n" + _)}
    |""".stripMargin
