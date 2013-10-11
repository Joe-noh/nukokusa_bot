package net.joe_noh.nukokusabot

import org.scalatest._
import scala.xml._
import java.util.Date

class TodaysTopicTest extends FlatSpec with PrivateMethodTester {

  "'fetchXML' method" should "fetch XML from mizunotomoaki.com correctly" in {
    val fetchXML = PrivateMethod[Elem]('fetchXML)
    val xml = TodaysTopic invokePrivate fetchXML(new Date)

    assert((xml \\  "dekigoto").length === 1)
    assert((xml \\ "tanjyoubi").length === 1)
    assert((xml \\     "imibi").length === 1)
    assert((xml \\   "kinenbi").length === 1)
    assert((xml \\      "item").length  != 0)
  }

  "'getTopic' method" should "take one topic" in {
    val result = TodaysTopic.getTopic(new Date)
    assert(result matches "^\\d{1,4}年\\d{1,2}月\\d{1,2}日 \\- .*$")
  }
}
