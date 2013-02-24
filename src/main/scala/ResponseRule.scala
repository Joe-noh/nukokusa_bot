package jp.nukokusabot

import twitter4j._

abstract class ResponseRule {

  def isMatch(status: Status): Boolean

  def respondTo(status: Status): Unit

}
