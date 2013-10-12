package net.joe_noh.nukokusabot

import org.scalatest._
import org.scalatest.PrivateMethodTester._

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

class MarkovChainTest extends FlatSpec with PrivateMethodTester with BeforeAndAfterAll {

  override def beforeAll(configMap: Map[String, Any]) {
    Tables.initialize
  }

  "Markov Chain" should "update the dictionary of chain correctly" in {
    val markov = new MarkovChain
    val updateDictionary = PrivateMethod[Unit]('updateDictionary)

    markov.invokePrivate(updateDictionary("あ え あ え い"))

    val words = Word.toList
    val links = Link.toList
    assert(words === List(Word("あ", 1), Word("え", 1), Word("い", 2)))
    assert(links === List(Link(1, 2, 2), Link(2, 1, 1), Link(2, 3, 1)))
  }

  "Tweet" should "work as bank" in {
    assert(Tweet.limit(2).toList == List("aa"))
  }

  override def afterAll(configMap: Map[String, Any]) {
    Tables.cleanup
  }
}
