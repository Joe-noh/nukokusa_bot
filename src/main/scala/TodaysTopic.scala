package jp.nukokusabot

import scala.io.Source
import scala.xml.XML
import java.util.Date

object TodaysTopic {

  def getTopic(date: Date): String = {
    val xml = fetchXML(date)
    return "a"
  }

  private def fetchXml(date: Date): XML = {
    val url = "http://www.mizunotomoaki.com/wikipedia_daytopic/api.cgi/%tm/%td".format(date)
    XML.loadString(Source.fromURL(url, "utf8").getLines.mkString)
  }

}
