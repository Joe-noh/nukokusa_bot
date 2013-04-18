package jp.nukokusabot

import com.github.aselab.activerecord._

case class Link(var sucId: Long, var times: Int = 1) extends ActiveRecord {
  var wordId: Option[Long] = None
  lazy val word = belongsTo[Word]
}

object Link extends ActiveRecordCompanion[Link]
