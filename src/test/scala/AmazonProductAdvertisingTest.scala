package jp.nukokusabot

import org.scalatest._

class AmazonProductAdvertisingTest extends FlatSpec {

  val amazon = new AmazonProductAdvertising

  "'getAllItems' method" should "fetch list of 10 products" in {
    assert(amazon.getAllItems("harry potter").length === 10)
  }

}
