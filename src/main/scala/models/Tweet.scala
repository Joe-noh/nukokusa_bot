package net.joe_noh.nukokusabot

import com.github.aselab.activerecord._

case class Tweet(tweetId: Long, text: String) extends ActiveRecord {}

object Tweet extends ActiveRecordCompanion[Tweet]
