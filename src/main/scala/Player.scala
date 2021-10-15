import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.File
import scala.io.StdIn.{readChar, readInt}
import java.io.{FileNotFoundException, IOException}
import scala.util.{Try, Success, Random}
import com.fasterxml.jackson.core.`type`.TypeReference

val r = scala.util.Random

object UI {
  def yesNoPrompt(): Boolean =
    val input = readChar()
    input match
      case 'Y' => true
      case 'y' => true
      case 'N' => false
      case 'n' => false
      case _   => yesNoPrompt()

  def listPrompt[A](list: Seq[A]): A =
    println(s"Please choose from the list below")
    for item <- list yield println(item)
    val input = readInt()
    if input > list.length then list(input)
    else listPrompt(list)
}

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

  def print() =
    s"""strength    : $strength
    |dexterity   : $dexterity
    |constitution: $constitution
    |intelligence: $intelligence
    |wisdom      : $wisdom
    |charisma    : $charisma""".stripMargin
}

sealed trait GameObject {
  private val mapper =
    YAMLMapper.builder().addModule(DefaultScalaModule).build()
  def save(file: File): Unit =
    if file.exists then
      println(
        s"File already exists at ${file.getCanonicalPath}. Would you like to overwrite it? (Y/y or N/n)"
      )
      UI.yesNoPrompt()
      mapper.writeValue(file, this)

  def load[A <: GameObject](file: File): Try[A] =
    Try(mapper.readValue(file, new TypeReference[A]() {}))
}

case class Task(
    name: String = "",
    description: String = "",
    difficulty: Int = 0,
    completed: Boolean = false,
    abilityScores: AbilityScores = AbilityScores()
)

case class Quest(tasks: Seq[Task] = Seq(Task())) extends GameObject {
  private def abilityScores = for task <- tasks yield task.abilityScores
  def exp = abilityScores.foldLeft(AbilityScores())(_ + _).sum
  def gold = tasks.length * r.nextInt(10) * tasks.map(_.difficulty).sum
}

abstract class Creature extends GameObject {
  def name: String
  def level: Int
  def exp: Float
  def health: Int
  def abilityScores: AbilityScores
}

case class Player(
    name: String = "Player",
    level: Int = 0,
    exp: Float = 0.0,
    health: Int = 100,
    val abilityScores: AbilityScores = AbilityScores()
) extends Creature {
  def completeQuest(exp: Float): Player =
    Player(name, level, this.exp + exp, health, abilityScores)

  def string: String =
    s"""------ Player -------
    |name   : $name
    |level  : $level
    |exp    : ${exp * 100}%
    |health : ($health/100)
    |--- Ability Scores --
    |$abilityScores
    |""".stripMargin
}
