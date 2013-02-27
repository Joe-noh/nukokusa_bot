package jp.nukokusabot

import scala.io.Source
import scala.util.Random
import scala.xml._

import java.util.Date
import java.text.SimpleDateFormat

object TodaysTopic {

  def getTopic(date: Date): String = {
    val random = new Random
    val topics = fetchXML(date) \ "dekigoto" \ "item"

    val topicText = topics(random.nextInt(topics.length)).text
    val topicDate = new SimpleDateFormat("M月d日").format(date)

    topicText.replaceFirst(" - ", topicDate+" - ")
  }

  private def fetchXML(date: Date): Elem = {
    val dateString = new SimpleDateFormat("MM/dd").format(date)
    val url = "http://www.mizunotomoaki.com/wikipedia_daytopic/api.cgi/" + dateString

    XML.loadString(Source.fromURL(url, "utf8").getLines.mkString)
  }

}
