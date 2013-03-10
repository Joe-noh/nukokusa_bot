package jp.nukokusabot

import scala.xml.XML
import java.util.Calendar

trait WeeklyJUMP {

  def getJUMPBuyer: String = {
    val buyers = Config.jumpBuyers

    val calendar = Calendar.getInstance
    val idx = calendar.get(Calendar.WEEK_OF_YEAR) % buyers.length

    return buyers(idx)
  }

}
