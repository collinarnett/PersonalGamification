import java.io.File
import org.scalatest.flatspec.AnyFlatSpec
import com.fasterxml.jackson.core.`type`.TypeReference
import java.util.Calendar
import java.util.GregorianCalendar

class PersonalGamificationSpec extends AnyFlatSpec:

  val cal = Calendar.getInstance()
  val r = scala.util.Random

  val mon = Monster(name="Mom", description="Your mom", toughness=6)
  val mons = Seq(mon)

  val itm1 = Item(name="Sword", description="Cuts things", affect=6, lifetime=cal)
  val itm2 = Item(name="Axe", description="Chops things", affect=3, lifetime=cal)
  val itm3 = Item(name="Lance", description="Pokes things", affect=4, lifetime=cal)
  val itms1 = Seq(itm1)
  val itms2 = Seq(itm1, itm2)
  val itms3 = Seq(itm1, itm2, itm3)

  val status1 = StatusEffect(name="Happy", description="Overwhelmed with happiness", affect=4, lifetime=cal)
  val status2 = StatusEffect(name="Depression", description="Despressed", affect= -2, lifetime=cal)
  val status3 = StatusEffect(name="Nerd", description="Maidenless", affect= -9, lifetime=cal)
  val statuses1 = Seq(status1)
  val statuses1Neg = Seq(status2)
  val statuses2 = Seq(status1, status2)
  val statuses2Neg = Seq(status2, status3)
  val statuses3 = Seq(status1, status2, status3)

  val newPlayer = Player(name="Justin")

  it should "Accurately reward the player (1 item, 1 status effect)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses1
        )

        val outcome = Outcome.reward(event, 4)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.statusEffects.last.affect >= 0)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (1 item, 1 status effects, but not selected since its negative)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses1Neg
        )

        val outcome = Outcome.reward(event, 3)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (2 items, 1 status effect)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms2,
          r.nextFloat,
          r.nextInt(10),
          statuses1
        )

        val outcome = Outcome.reward(event, 1)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (3 items, 1 status effect)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms3,
          r.nextFloat,
          r.nextInt(10),
          statuses1
        )

        val outcome = Outcome.reward(event, 7)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (1 item, 2 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses2
        )

        val outcome = Outcome.reward(event, 6)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (1 item, 2 status effects, but since both negative none are selected" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses2Neg
        )

        val outcome = Outcome.reward(event, 8)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (1 item, 3 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses3
        )

        val outcome = Outcome.reward(event, 2)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately reward the player (2 items, 2 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms2,
          r.nextFloat,
          r.nextInt(10),
          statuses2
        )

        val outcome = Outcome.reward(event, 9)
        newPlayer ++ outcome

      assert(updatedPlayer.items.size >= 1)
      assert(updatedPlayer.health >= newPlayer.health)
      assert(!updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately punish the player (1 item, 3 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms1,
          r.nextFloat,
          r.nextInt(10),
          statuses3
        )

        val outcome = Outcome.punishment(event, 3)
        newPlayer ++ outcome

      assert(updatedPlayer.health <= newPlayer.health)
      assert(updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately punish the player (2 items, 3 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms2,
          r.nextFloat,
          r.nextInt(10),
          statuses3
        )

        val outcome = Outcome.punishment(event, 10)
        newPlayer ++ outcome

      assert(updatedPlayer.health <= newPlayer.health)
      assert(updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }

  it should "Accurately punish the player (3 items, 3 status effects)" in{
    
    val updatedPlayer: Player =
        // Generate Event
        val event = Event(
          mons,
          itms2,
          r.nextFloat,
          r.nextInt(10),
          statuses3
        )

        val outcome = Outcome.punishment(event, 5)
        newPlayer ++ outcome

      assert(updatedPlayer.health <= newPlayer.health)
      assert(updatedPlayer.items.isEmpty)
      assert(!updatedPlayer.statusEffects.isEmpty)
  }