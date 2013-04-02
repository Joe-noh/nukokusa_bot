package jp.nukokusabot

import org.scalatest._

class MarkovChainTest extends FlatSpec {

  "Markov Chain" should "generate sentence rondamly" in {
    val markov = new MarkovChain
    markov.addSentence("あいうえお")

    val sentence = markov.generateSentence()

    assert("あいうえお" contains sentence)
  }

}
