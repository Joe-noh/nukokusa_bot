package jp.nukokusabot

import java.util.Properties

trait WeeklyJUMP {
  implicit def any2tap[A](obj: A): Tap[A] = new Tap(obj)

  def getJUMPBuyer = {
    val buyers = new Properties().tap{_.load(getClass.getResourceAsStream("/jump.properties"))}
//    buyers.find(_.get == 1)
  }

}
