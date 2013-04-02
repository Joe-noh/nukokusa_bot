package jp.nukokusabot

import net.reduls.gomoku._

class Word(val surface: String, val feature: String)

object Word {
  def apply(m: Morpheme) = new Word(m.surface, m.feature.substring(0, 2))
}
