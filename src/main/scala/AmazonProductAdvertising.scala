package net.joe_noh.nukokusabot

import scala.io.Source
import scala.xml.XML
import scala.collection.immutable.TreeMap

class AmazonProductAdvertising extends SignedRequestsAmazonApi {

  def getAllItems(keyword: String) = {
    val params = TreeMap("Operation"     -> "ItemSearch",
                         "SearchIndex"   -> "All",
                         "Availability"  -> "Available",
                         "ResponseGroup" -> "Large",
                         "Keywords"      -> keyword)
    val requestUrl = sign(params)

    XML.loadString(Source.fromURL(requestUrl).getLines.mkString) \\ "Item"
  }

  def getFirstItem(keyword: String) = new AmazonProduct(getAllItems(keyword).head)
}
