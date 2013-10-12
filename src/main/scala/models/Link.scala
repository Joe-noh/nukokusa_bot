package net.joe_noh.nukokusabot

import com.github.aselab.activerecord._

case class Link(preId: Long,
                sucId: Long,
                var count: Int = 0) extends ActiveRecord {}

object Link extends ActiveRecordCompanion[Link]
