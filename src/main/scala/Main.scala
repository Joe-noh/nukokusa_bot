package net.joe_noh.nukokusabot

object Main {

  def main(args: Array[String]) = {
    try {
      // Tables.initialize

      if (args.contains("--regenerate-dictionary")) {
        val markov = new MarkovChain
        markov.rebuildDictionary
      }

      val bot = new NukokusaBot
      bot.run
    } catch {
      case e: Exception => // TODO
        println(e.getMessage)
    } finally {
      // Tables.cleanup
    }
  }
}
