package jp.nukokusabot

import scala.actors.Actor
import twitter4j._

object Responder extends Actor {

  private var rules = Seq.empty[ResponseRule]

  def addResponseRule(rule: ResponseRule) = {
    rules = rules :+ rule
  }

  def act = {
    loop {
      react {
	case status: Status =>
	  println(status.getText())
	case _ => {}
      }
    }
  }

}
