package jp.nukokusabot

import org.scalatest._
import java.util.Properties

class WeeklyJUMPTest extends FlatSpec with WeeklyJUMP {

  val prop = new Properties()
  prop.setProperty("index", 0)
  prop.setProperty("member", List("a", "b", "c"))

  "'getJUMPBuyer' method" should "get a lab member who should buy JUMP" in {
    val buyer = getJUMPBuyer
    assert(prop.get(member) === "hey")
  }

}
