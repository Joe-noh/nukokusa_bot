package jp.nukokusabot

import scala.util.Random
import scala.collection.JavaConversions._

import net.reduls.gomoku._

class MarkovChain(val dbFile: String = "markov_chain.db") {

  Dictionary.initialize(dbFile)

  def addSentence(sentence: String): Unit = {
    val words = Tagger.parse(sentence).map(Word(_)).toList

    Dictionary.registerWords(words)
    Dictionary.registerLinks(words)
  }

  def generateSentence(limit: Int = 140): String = {
    def challenge: String = {
      def make(preID: Int, idAcc: List[Int]): List[Int] = {
        val id = Dictionary.fetchNextID(preID)
        id match {
          case i: Int if i <= 1 => idAcc
          case _: Int  => make(id, idAcc :+ id)
        }
      }

      val (id, surface) = Dictionary.randomChoice
      val sentence = Dictionary.translateToJapanese(make(id, List(id)))
      sentence match {
	case _ if (sentence.length == 0 || sentence.length > limit) => challenge
	case _ => sentence
      }
    }

    challenge
  }

}
