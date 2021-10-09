import scala.compiletime.ops.string
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.File
import scala.language.implicitConversions

case class AbilityScores(
    strength: Int = 0,
    dexterity: Int = 0,
    constitution: Int = 0,
    intelligence: Int = 0,
    wisdom: Int = 0,
    charisma: Int = 0
) {

  def +=(abilityScores: AbilityScores): AbilityScores =
    AbilityScores(
      strength + abilityScores.strength,
      dexterity + abilityScores.dexterity,
      constitution + abilityScores.constitution,
      intelligence + abilityScores.intelligence,
      wisdom + abilityScores.wisdom,
      charisma + abilityScores.charisma
    )
}

sealed trait GameObject {
  private val mapper =
    YAMLMapper.builder().addModule(DefaultScalaModule).build()
  def save(filename: String): Unit =
    mapper.writeValue(new File(filename), this)
  def load(filename: String): GameObject =
    mapper.readValue(new File(filename), this.getClass)
}

case class Task(name: String, description: String, abilityScores: AbilityScores)

case class Player(
    name: String = "Trexd",
    level: Int = 0,
    exp: Double = 0.0,
    health: Int = 100,
    val abilityScores: AbilityScores = AbilityScores()
) extends GameObject {
  given Conversion[ArgumentType, String] = _.toString
  def this(filename: String) =
    this()
    load(filename)
  def completeTask(task: Task) =
    println(s"$name completed task ${task.name}")
    Player(
      this.name,
      this.level,
      this.exp,
      this.health,
      this.abilityScores += task.abilityScores
    )
}
