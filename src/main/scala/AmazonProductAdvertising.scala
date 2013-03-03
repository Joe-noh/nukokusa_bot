package jp.nukokusabot

import scala.io.Source
import scala.xml.XML
import scala.collection.immutable.TreeMap

class AmazonProductAdvertising extends SignedRequestsAmazonApi {

  def search(keyword: String) = {
    val params = TreeMap("Operation"   -> "ItemSearch",
                         "SearchIndex" -> "All",
                         "Keywords"    -> keyword)
    val requestUrl = sign(params)

    XML.loadString(Source.fromURL(requestUrl).getLines.mkString) \\ "Item"
  }
}
