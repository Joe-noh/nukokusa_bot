package jp.nukokusabot

import scala.util.Random
//import scala.collection.mutable._
import scala.collection.JavaConversions._

import net.reduls.gomoku._

class MarkovChain(val dbFile: String = "markov_chain.db") {

/*  private var atom_id  = Map[String, Int]()
  private var tailings = Map[Int, List[Int]]()
  private var max_id = -1
 */
  def addSentence(sentence: String): Unit = {
    val words = Tagger.parse(sentence).map(Word(_)).toArray//m => m.surface + m.feature.substring(0, 2))
    val iterator = words.sliding(2)

    registerWords(words)
    registerLinks(iterator)
  }

  def generateSentence(limit: Int = 140): String = {
/*    val dict = atom_id.map(_.swap)

    def challenge: String = {
      val sentence = getChain.map(dict(_).dropRight(2)).mkString
      sentence match {
	case _ if (sentence.length == 0 || sentence.length > limit) => challenge
	case _ => sentence
      }
    }
    challenge*/
    "a"
  }

  private def getChain: List[Int] = {
/*    def chain(atom_id: Int, acc: List[Int]): List[Int] = atom_id match {
      case _ if atom_id == 0 => acc
      case _ => {
	val candidates = tailings(atom_id)
	val next = candidates(Random.nextInt(candidates.length))
	chain(next, next :: acc)
      }
    }

    chain(Random.nextInt(max_id)+1, List[Int]()).reverse
 */ List(1,2)
  }

  private def registerWords(words: Array[Word]): Unit = {
    for (word <- words) {
      Dictionary.appendWord(word)
    }
  }

  private def registerLinks(iterator: Iterator[Array[Word]]): Unit = {
    while (iterator.hasNext) {
      aggregateTailing(iterator.next)
    }
  }

  private def aggregateTailing(pair: Array[Word]): Unit = {
    //var Array(pre, suc) = pair.map(atom_id(_))
    //tailings(pre) :+= suc
  }

  private def show = {
/*    for ((atom, id) <- atom_id) {
      println(List(atom, " : ", id.toString).mkString)
    }
    for ((pre, sucs) <- tailings) {
      println(pre + sucs.mkString("[", ", ", "]"))
    }*/
  }

}
