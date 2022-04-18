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
import java.util.Calendar
import java.util.Date

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
    level: Int = 0,
    exp: Float = 0.0,
    items: Seq[Items],
    health: Int = 100,
    val abilityScores: AbilityScores = AbilityScores()
) extends GameObject {
  def ++(outcome: Outcome): Player =
    Player(
      name,
      exp + outcome.exp,
      items ++ outcome.items,
      health ++ outcome.health
    )

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
