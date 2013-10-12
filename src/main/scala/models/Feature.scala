package net.joe_noh.nukokusabot

import com.github.aselab.activerecord._

case class Feature(tag: String) extends ActiveRecord {}

object Feature extends ActiveRecordCompanion[Feature]
