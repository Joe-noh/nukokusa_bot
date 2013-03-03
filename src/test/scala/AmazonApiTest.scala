package jp.nukokusabot

import org.scalatest._
import scala.collection.immutable.TreeMap

class AmazonAPITest extends FlatSpec with SignedRequestsAmazonApi{

  "Amazon helper" should "give us a request URL" in {
    var params = TreeMap("Operation"   -> "ItemSearch",
                         "SearchIndex" -> "Books",
                         "Keywords"    -> "harry potter")
    val requestUrl = sign(params)

    assert(requestUrl.matches("^http://ecs\\.amazonaws\\.com/onca/xml\\?.+$"))
  }

}
