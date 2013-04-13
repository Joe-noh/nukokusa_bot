package jp.nukokusabot

import org.scalatest._

class MarkovChainTest extends FlatSpec {

  "Markov Chain" should "generate sentence rondamly" in {
    val markov = new MarkovChain("for_test.db")
    markov.addSentence("あいうえお")

    val sentence = markov.generateSentence()

//    Dictionary.scrap
    assert("あいうえお" === sentence)
  }

}
