package jp.nukokusabot

import com.github.aselab.activerecord._

case class Word(var surface: String, var feature: String) extends ActiveRecord {
  lazy val links = hasMany[Link]
}

object Word extends ActiveRecordCompanion[Word]
