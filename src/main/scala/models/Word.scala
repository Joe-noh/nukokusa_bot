package net.joe_noh.nukokusabot

import com.github.aselab.activerecord._

case class Word(surface: String, featureId: Int) extends ActiveRecord {}

object Word extends ActiveRecordCompanion[Word]
