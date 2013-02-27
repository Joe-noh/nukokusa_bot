package jp.nukokusabot

import java.util.Calendar

class Schedule(val hourFrom: Int, val hourTo: Int, val minute: Int) {

  def isMatch(calendar: Calendar): Boolean = {
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val min  = calendar.get(Calendar.MINUTE)

    return (hourFrom <= hour && hour <= hourTo && min == minute)
  }

  def task: Unit = {}

}
