package jp.nukokusabot

import org.scalatest._

class AmazonProductAdvertisingTest extends FlatSpec {

  "'search' method" should "fetch list of products" in {
    val amazon = new AmazonProductAdvertising
    val list = amazon.search("harry potter")

    assert(list.length === 10)
  }

}
