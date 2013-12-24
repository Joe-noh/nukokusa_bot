package net.joe_noh.nukokusabot

import scala.xml.XML
import java.util.Calendar

trait WeeklyJUMP {

  def getJUMPBuyerName(offset:Int = 0): String = {
    val buyers = Config.jumpBuyers

    val calendar = Calendar.getInstance
    val idx = (calendar.get(Calendar.WEEK_OF_YEAR) + offset) % buyers.length

    return buyers(idx)
  }

  def getNextJUMPBuyerName(offset:Int = 0): String = {
    val buyers = Config.jumpBuyers

    val calendar = Calendar.getInstance
    val idx = (calendar.get(Calendar.WEEK_OF_YEAR) + 1+offset) % buyers.length

    return buyers(idx)
  }

}
