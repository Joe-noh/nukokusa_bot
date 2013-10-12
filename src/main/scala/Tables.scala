package net.joe_noh.nukokusabot

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

object Tables extends ActiveRecordTables {
  val words = table[Word]
  val links = table[Link]
  val features = table[Feature]
  val tweets = table[Tweet]

  on(words)(t => declare(
              columns(t.surface, t.featureId) are(unique)
            ))

  on(links)(t => declare(
              t.count defaultsTo(1)
            ))

  on(features)(t => declare(
              t.tag is(unique)
            ))

  on(tweets)(t => declare(
              t.tweetId is(unique)
            ))
}
