package net.joe_noh.nukokusabot

import scala.collection.immutable.Range
import java.util.Calendar

abstract class Schedule {
  var wdayRange = Calendar.SUNDAY to Calendar.SATURDAY
  var dateRange = 1 to 31
  var hourRange = 0 to 23
  var minRange  = 0 to 59

  def isMatch(calendar: Calendar): Boolean = {
    val wday = calendar.get(Calendar.DAY_OF_WEEK)
    val date = calendar.get(Calendar.DATE)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val min  = calendar.get(Calendar.MINUTE)

    return (wdayRange.contains(wday) &&
            dateRange.contains(date) &&
            hourRange.contains(hour) &&
             minRange.contains( min))
  }

  def task: Unit

}
