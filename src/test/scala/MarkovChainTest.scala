package jp.nukokusabot

import org.scalatest._

class MarkovChainTest extends FlatSpec {

  "Markov Chain" should "generate random sentence" in {

    val markov = new MarkovChain
    val example = "これはテストのための例文です。"
    markov.addSentence(example)

    val sentence = markov.generateSentence()

    Dictionary.dropAll
    //Dictionary.close
    assert(example === sentence)
    assert(example contains sentence)
  }

}
