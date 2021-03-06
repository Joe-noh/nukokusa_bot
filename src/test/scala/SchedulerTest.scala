package net.joe_noh.nukokusabot

import org.scalatest._
import scala.collection.immutable.Range
import java.util.Calendar

class SchedulerTest extends FlatSpec {

  val midnight = new Schedule { def task = {} }
  midnight.minRange  = 0 to 0
  midnight.hourRange = 0 to 0

  val sundayMorning = new Schedule {def task = {} }
  sundayMorning.minRange  = 0 to 0
  sundayMorning.hourRange = 7 to 7
  sundayMorning.wdayRange = Calendar.SUNDAY to Calendar.SUNDAY

  "'midnight' schedule" should "match only 0:00" in {
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

  "'sundayMorning' schedule" should "match only every sunday's 7:00" in {
    val clock = Calendar.getInstance
    clock.set(Calendar.HOUR_OF_DAY,  7)
    clock.set(Calendar.MINUTE,       0)

    for (wday <- Calendar.SUNDAY to Calendar.SATURDAY) {
      clock.set(Calendar.DAY_OF_WEEK, wday)
      assert(sundayMorning.isMatch(clock) === (
               wday match {
                 case i:Int if i==Calendar.SUNDAY => true
                 case _ => false
               }
             )
      )
    }
  }

}
