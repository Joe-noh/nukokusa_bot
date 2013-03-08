package jp.nukokusabot

import scala.xml.XML
import java.util.Calendar

trait WeeklyJUMP {

  def getJUMPBuyer: String = {
    val conf = XML.load(getClass.getResource("/config.xml"))
    val buyers = conf \\ "buyers"

    val calendar = Calendar.getInstance
    val idx = calendar.get(Calendar.WEEK_OF_YEAR) % buyers.length

    return buyers(idx).text
  }

}
