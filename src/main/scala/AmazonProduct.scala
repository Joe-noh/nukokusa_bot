package jp.nukokusabot

import scala.xml._

class AmazonProduct(xml: Node) {

  val title = (xml \ "ItemAttributes" \\ "Title").text
  val price = (xml \ "ItemAttributes" \\ "Amount").text
  val url   = (xml \\ "DetailPageURL").text

}
