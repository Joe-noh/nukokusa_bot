package jp.nukokusabot

import org.scalatest._

class WeeklyJUMPTest extends FlatSpec with WeeklyJUMP {

  "'getJUMPBuyerName' method" should "get a lab member who should buy JUMP" in {
    assert(getJUMPBuyerName === "hey")
  }

}
