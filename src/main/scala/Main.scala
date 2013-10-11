package net.joe_noh.nukokusabot

object Main {

  def main(args: Array[String]) = {
    try {
      val bot = new NukokusaBot
      bot.run
    } catch {
      case e: Exception => // TODO
        println(e.getMessage)
    }
  }
}
