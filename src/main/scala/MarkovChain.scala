package jp.nukokusabot

import net.reduls.gomoku._

import scala.util.Random
import scala.collection.mutable._
import scala.collection.JavaConversions._

class MarkovChain {

  private var atom_id  = Map[String, Int]()
  private var tailings = Map[Int, List[Int]]()
  private var max_id = -1

  registerAtoms(Array("末尾"))

  def addSentence(sentence: String): Unit = {
    val atoms = Tagger.parse(sentence).map(m => m.surface + m.feature.substring(0, 2)).toArray

    registerAtoms(atoms)

    val iterator = atoms.sliding(2)
    while (iterator.hasNext) { aggregateTailing(iterator.next) }
    aggregateTailing(Array(atoms.last, "末尾"))
  }

  def generateSentence(limit: Int = 140): String = {
    val dict = atom_id.map(_.swap)

    def challenge: String = {
      val sentence = getChain.map(dict(_).dropRight(2)).mkString
      sentence match {
	case _ if (sentence.length == 0 || sentence.length > limit) => challenge
	case _ => sentence
      }
    }
    challenge
  }

  private def getChain: List[Int] = {
    def chain(atom_id: Int, acc: List[Int]): List[Int] = atom_id match {
      case _ if atom_id == 0 => acc
      case _ => {
	val candidates = tailings(atom_id)
	val next = candidates(Random.nextInt(candidates.length))
	chain(next, next :: acc)
      }
    }

    chain(Random.nextInt(max_id)+1, List[Int]()).reverse
  }

  private def registerAtoms(atoms: Array[String]): Unit = {
    for (atom <- atoms) {
      if (!atom_id.contains(atom)) {
	max_id += 1
	atom_id.update(atom, max_id)
	tailings(max_id) = List[Int]()
      }
    }
  }

  private def aggregateTailing(pair: Array[String]): Unit = {
    var Array(pre, suc) = pair.map(atom_id(_))
    tailings(pre) :+= suc
  }

  private def show = {
    for ((atom, id) <- atom_id) {
      println(List(atom, " : ", id.toString).mkString)
    }
    for ((pre, sucs) <- tailings) {
      println(pre + sucs.mkString("[", ", ", "]"))
    }
  }
}
