package net.joe_noh.nukokusabot

import org.scalatest._
import scala.collection.immutable.TreeMap

class AmazonHelperTest extends FlatSpec with SignedRequestsAmazonApi {

  "Amazon helper" should "give us a request URL" in {
    val params = TreeMap("Operation"   -> "ItemSearch",
                         "SearchIndex" -> "Books",
                         "Keywords"    -> "harry potter")
    val requestUrl = sign(params)

    assert(requestUrl.matches("^http://webservices\\.amazon\\.co\\.jp/onca/xml\\?.+$"))
  }

}
