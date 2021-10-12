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

  def +(abilityScores: AbilityScores): AbilityScores =
    AbilityScores(
      strength + abilityScores.strength,
      dexterity + abilityScores.dexterity,
      constitution + abilityScores.constitution,
      intelligence + abilityScores.intelligence,
      wisdom + abilityScores.wisdom,
      charisma + abilityScores.charisma
    )
  override def toString() =
    s"""
    |  strength    : $strength
    |  dexterity   : $dexterity
    |  constitution: $constitution
    |  intelligence: $intelligence
    |  wisdom      : $wisdom
    |  charisma    : $charisma
    |---------------------
    """
}

sealed trait GameObject {
  private val mapper =
    YAMLMapper.builder().addModule(DefaultScalaModule).build()
  def save(file: File): Unit =
    mapper.writeValue(file, this)
  def load(file: File): GameObject =
    mapper.readValue(file, this.getClass)
}

case class Task(
    name: String = "",
    description: String = "",
    abilityScores: AbilityScores = AbilityScores()
)

case class Player(
    file: File = File("player.yaml"),
    name: String = "Player",
    level: Int = 0,
    exp: Float = 0.0,
    health: Int = 100,
    val abilityScores: AbilityScores = AbilityScores()
) extends GameObject {
  def this(file: File) =
    this()
    load(file)
  def completeTask(task: Task) =
    Player(
      this.file,
      this.name,
      this.level,
      this.exp,
      this.health,
      this.abilityScores + task.abilityScores
    )
  override def toString() =
    s"""
    |------ Status -------
    |  name   : $name
    |  level  : $level
    |  exp    : ${exp * 100}%
    |  health : ($health/100)
    |---------------------
    |-- Ability Scores ---
    |$abilityScores

    """

}
