package jp.nukokusabot

object Main {

  def main(args: Array[String]) = {
    try {
      args(0) match {
        case opt: String if opt == "--run" =>
          val bot = new NukokusaBot
          bot.run
        /*case opt: String if opt == "--parse-tweets-zip" =>
          val markov = new MarkovChain
          markov.parseTweetsZip*/
        case _ =>
          println(args(0))
          exit
      }
    } catch {
      case e: Exception => // TODO
        println(e.getMessage)
    } finally {
      //println("fin")
      //Dictionary.close
    }
  }
}
