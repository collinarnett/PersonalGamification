case class AbilityScores(
    strength: Int = 0,
    dexterity: Int = 0,
    constitution: Int = 0,
    intelligence: Int = 0,
    wisdom: Int = 0,
    charisma: Int = 0
) {
  def +(abilityScores: AbilityScores): AbilityScores = {
    AbilityScores(
      strength = strength + abilityScores.strength,
      dexterity = dexterity + abilityScores.dexterity,
      constitution = constitution + abilityScores.constitution,
      intelligence = intelligence + abilityScores.intelligence,
      wisdom = wisdom + abilityScores.wisdom,
      charisma = charisma + abilityScores.charisma
    )
  }
}

case class Task(name: String, abilityScores: AbilityScores)

case class Player(
    name: String = "Trexd",
    level: Int = 0,
    exp: Double = 0.0,
    health: Int = 100,
    var abilityScores: AbilityScores = AbilityScores()
) {
  def completeTask(task: Task): Unit = {
    this.abilityScores += task.abilityScores
    println(s"$name completed task ${task.name}")
  }

}
