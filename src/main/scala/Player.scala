import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.File
import scala.io.StdIn.{readChar, readInt}
import java.io.{FileNotFoundException, IOException}
import scala.util.{Try, Success, Random}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.ClassTagExtensions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

sealed trait GameObject {
  private val mapper =
    new YAMLMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  def save(file: File): Unit =
    if file.exists then
      println(
        s"File already exists at ${file.getCanonicalPath}. Would you like to overwrite it? (Y/y or N/n)"
      )
      mapper.writeValue(file, this)
}

case class Task(
    name: String = "",
    description: String = "",
    dueDate: String = "",
    difficulty: Int = 0,
    abilityScores: AbilityScores = AbilityScores()
)

case class Player(
    name: String = "Player",
    level: Int = 0,
    exp: Float = 0.0,
    health: Int = 100,
    val abilityScores: AbilityScores = AbilityScores()
) extends GameObject {
  def completeTask(exp: Float): Player =
    Player(name, level, this.exp + exp, health, abilityScores)

  def string: String =
    s"""------ Player -------
    |name   : $name
    |level  : $level
    |exp    : ${exp * 100}%
    |health : ($health/100)
    |--- Ability Scores --
    |${abilityScores.string}
    |""".stripMargin
}
