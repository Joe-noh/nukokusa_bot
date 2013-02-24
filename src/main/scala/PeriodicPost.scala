package jp.nukokusabot

import scala.actors.Actor

import twitter4j._

class PeriodicPost(interval: Int) extends Actor {
  val postInterval = interval*60*1000

  def act = {
    loop {
      reactWithin(postInterval) {
	case scala.actors.TIMEOUT =>
	  println("periodic post")
	case _ => println("got message")
      }
    }
  }
}
