package jp.nukokusabot

import org.scalatest._
import scala.collection.immutable.Range
import java.util.Calendar

class SchedulerTest extends FlatSpec {

  val midnight = new Schedule { def task = {} }
  midnight.minRange  = 0 to 0
  midnight.hourRange = 0 to 0

  val monday = new Schedule {def task = {} }
  monday.minRange  = 0 to 0
  monday.hourRange = 7 to 7
  monday.wdayRange = Calendar.MONDAY to Calendar.MONDAY

  "'midnight' schedule" should "match 0:00 everyday" in {
    val clock = Calendar.getInstance
    clock.set(Calendar.HOUR_OF_DAY,  0)
    clock.set(Calendar.MINUTE,       0)

    for (day <- 1 to 31) {
      clock.set(Calendar.DATE, day)
      assert(midnight.isMatch(clock) === true)
    }

    clock.set(Calendar.MINUTE, 1)
    assert(midnight.isMatch(clock) === false)
  }

  "'monday' schedule" should "match 7:00 every monday" in {
    val clock = Calendar.getInstance
    clock.set(Calendar.HOUR_OF_DAY,  7)
    clock.set(Calendar.MINUTE,       0)

    for (wday <- Calendar.SUNDAY to Calendar.SATURDAY) {
      clock.set(Calendar.DAY_OF_WEEK, wday)
      assert(monday.isMatch(clock) === (wday match {
                                          case i:Int if i==Calendar.MONDAY => true
                                          case _ => false
                                        }))
    }
  }

}
