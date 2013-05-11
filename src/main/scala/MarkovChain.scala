/*package jp.nukokusabot

import scala.util.Random
import scala.collection.JavaConversions._

import net.reduls.gomoku._

class MarkovChain {

  Dictionary.open

  def addSentence(sentence: String): Unit = {
    val words = Tagger.parse(sentence).map(m => (m.surface, m.feature.substring(0, 2))).toList

    Dictionary.registerWords(words)
    Dictionary.registerLinks(words)
  }

  def generateSentence(limit: Int = 140): String = {
    def challenge: String = {
      def make(preID: Long, idAcc: List[Long]): List[Long] = {
        val id = Dictionary.fetchNextID(preID)
        id match {
          case i: Long if i <= 2 => idAcc
          case _: Long  => make(id, idAcc :+ id)
        }
      }

      val id = Dictionary.randomChoice
      val sentence = Dictionary.translateToJapanese(make(id, List(id)))
      sentence match {
	case _ if (sentence.length == 0 || sentence.length > limit) => challenge
	case _ => sentence
      }
    }

    challenge
  }

  def parseTweetsZip = {}

}
 */
