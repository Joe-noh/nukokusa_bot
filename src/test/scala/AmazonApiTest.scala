package jp.nukokusabot

import org.scalatest._
import scala.collection.immutable.TreeMap

class AmazonAPITest extends FlatSpec with SignedRequestsAmazonApi{

  "Amazon helper" should "lookup some items" in {
    var params = TreeMap.empty[String, String]
    params = params.insert("Operation", "ItemSearch")
    params = params.insert("SearchIndex", "Books")
    params = params.insert("Keywords", "harry potter")

    val requestUrl = sign(params);
    Console.out.println("Signed Request is \"" + requestUrl + "\"");

    assert(requestUrl.matches("^http://ecs\\.amazonaws\\.com/onca/xml\\?.+$"))
  }

}
