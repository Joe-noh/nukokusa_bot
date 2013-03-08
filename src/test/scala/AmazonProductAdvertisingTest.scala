package jp.nukokusabot

import org.scalatest._

class AmazonProductAdvertisingTest extends FlatSpec {

  val amazon = new AmazonProductAdvertising

  "'getAllItems' method" should "fetch list of 10 products" in {
    assert(amazon.getAllItems("harry potter").length === 10)
  }

  "'getFirstItem' method" should "fetch only one item" in {
    val item = amazon.getFirstItem("ハリーポッター")

    assert(item.url matches "^http://www\\.amazon\\.co\\.jp.*$")
    assert(item.price matches "^\\d+$")

  }
}
