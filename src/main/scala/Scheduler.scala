package jp.nukokusabot

import scala.actors.Actor
import java.util.Calendar
import twitter4j._

class Scheduler(schedules: List[Schedule]) extends Actor {
  val interval = 60*1000  // 1 minute

  def act = {
    loop {
      reactWithin(interval) {
	case scala.actors.TIMEOUT =>
	  val calendar = Calendar.getInstance

	  schedules.find(_.isMatch(calendar)) match {
	    case Some(schedule) => schedule.task
	    case None => ()
	  }
	case _ => ()
      }
    }
  }
}
