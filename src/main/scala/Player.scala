import scala.compiletime.ops.string
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.File
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

  def this(filename: String) =
    this()
    load(filename)

  def completeTask(task: Task) =
    this.abilityScores += task.abilityScores
    println(s"$name completed task ${task.name}")

  def save(filename: String) =
    val mapper = JsonMapper.builder().addModule(DefaultScalaModule).build()
    mapper.writeValue(new File(filename), this)

  def load(filename: String): Player =
    val mapper = JsonMapper.builder().addModule(DefaultScalaModule).build()
    mapper.readValue(new File(filename), classOf[Player])

}
