package jp.nukokusabot

import scala.xml.XML

object Config {

  val conf = XML.load(getClass.getResourceAsStream("/config.xml"))

  val debug = if ((conf \\ "debug").text == "1") true else false

  val awsSecretKey    = (conf \\ "secretAccessKey").text
  val awsAccessKeyId  = (conf \\ "accessKeyId").text
  val awsAssociateTag = (conf \\ "associateTag").text

  val jumpBuyers = (conf \\ "buyers").map(_.text)

}
