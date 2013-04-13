package jp.nukokusabot

import org.scalatest._

class UtilsTest extends FlatSpec {

  "'timestamp' method" should "get current timestamp" in {
    assert(Utils.timestamp matches "^\\[\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\]$")
  }

}
