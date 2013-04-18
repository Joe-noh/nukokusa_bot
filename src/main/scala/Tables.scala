package jp.nukokusabot

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

object Tables extends ActiveRecordTables {
  val words = table[Word]
  val links = table[Link]

  on(words)(t =>
    declare(t.surface is(dbType("varchar(20)")),
            t.feature is(dbType("char(2)")),
            columns(t.surface, t.feature) are(unique)))
}
