package net.joe_noh.nukokusabot

import twitter4j._
import scala.util.Random
import scala.collection.JavaConversions._

import net.reduls.gomoku._

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

class MarkovChain {

  val random = new Random()

  def addStatus(status: Status): Unit = {
    if (!Tweet.exists(_.tweetId === status.getId())) {
      Tweet(status.getId(), status.getText()).save()
      updateDictionary(status.getText())
    }
  }

  def addStatus(id: Long, text: String): Unit = {
    if (!Tweet.exists(_.tweetId === id)) {
      Tweet(id, text).save()
      updateDictionary(text)
    }
  }

  def rebuildDictionary = {
    Word.deleteAll
    Link.deleteAll
    (0 to (Tweet.count/100).toInt).foreach { i =>
      Tweet.page(100*i, 100).foreach { status  =>
        addStatus(status.id, status.text)
      }
    }
  }

  def generateSentence(limit: Int = 140): String = {
    def challenge: String = {
      def buildChain(preId: Long, idAcc: List[Long]): List[Long] = {
        val nextId = fetchNextId(preId)
        random.nextDouble match {
          case d: Double if d < 0.1 => idAcc
          case _: Double  => buildChain(nextId, idAcc :+ nextId)
        }
      }

      val id = random.nextLong() % Word.count
      val sentence = translateChainToJapanese(buildChain(id, List(id)))
      sentence match {
      	case _ if (sentence.length == 0 || sentence.length > limit) => challenge
        case _ => sentence
      }
    }
    challenge
  }

  private def translateChainToJapanese(chain: List[Long]) = {
    def translate(chain: List[Long], stringAcc: String): String = chain match {
      case head::rest => translate(rest, stringAcc + Word.find(head).get.surface)
      case _ => stringAcc
    }
    translate(chain, "")
  }


  private def updateDictionary(sentence: String): Unit = {
    val pairList = Tagger.parse(sentence).map(m => (m.surface, m.feature)).toList

    val words = pairList.map{ p =>
      val feature = Feature.findByOrCreate(Feature(p._2), "tag")
      Word.findByOrCreate(Word(p._1, feature.id.asInstanceOf[Int]), "surface", "featureId")
    }

    words.sliding(2).foreach{ case List(pre, suc) =>
      val link = Link.findByOrCreate(Link(pre.id, suc.id), "preId", "sucId")
      link.count += 1
      link.save()
    }
  }

  private def fetchNextId(wordId: Long): Long = {
    var candidates = Link.findBy("preId", wordId).toList.collect{ case link => List.fill(link.count)(link.sucId) }.flatten
    random.shuffle(candidates).get(0)
  }
}
