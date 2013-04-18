package jp.nukokusabot

import scala.util.Random
import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

object Dictionary extends Extends {
  val rand = new Random

  def open: Unit = {
    Tables.initialize

    Word.findByOrCreate(Word("", "末尾"), "surface", "feature")
  }

  def fetchNextID(id: Long): Long = {
    val applicants = Link.findBy("wordId", id).toList.map(link => List(link.sucId) * link.times).flatten

    if (applicants.length > 0) {
      return applicants(rand.nextInt(applicants.length))
    } else {
      return 1
    }
  }

  def randomChoice: Long = {
    val maxID = Word.max(_.id).get - 1
    Word.find((rand.nextLong % maxID) + 2) match {
      case Some(rec) => rec.id
      case None => 1
    }
  }

  def registerWords(words: List[(String, String)]): Unit = {
    for (word <- words) {
      Word.findByOrCreate(Word(word._1, word._2), "surface", "feature").save
    }
  }

  def registerLinks(words: List[(String, String)]): Unit = {
    val idList = (words.map(w => fetchID(Word.findBy("surface" -> w._1, "feature" -> w._2).get)) :+ 1L).sliding(2)
    for (List(pre, suc) <- idList) {
      if (Link.where(_.wordId === pre).exists(_.sucId == suc)) {
        val link = Link.findBy("wordId" -> pre, "sucId" -> suc).get
        link.times += 1
        link.save
      } else {
        val word = Word.find(pre).get
        word.links << Link(suc)
      }
    }
  }

  def translateToJapanese(idList: List[Long]): String = {
    Word.where(_.id in idList).toList.sortBy(idList indexOf _.id).map(_.surface).mkString
  }

  def dropAll = {
    Word.deleteAll
    Link.deleteAll
    Tables.cleanup
  }

  def close = Tables.cleanup

  private def fetchID(word: Word): Long = {
    Word.findBy("surface" -> word.surface, "feature" -> word.feature) match {
      case Some(record) => record.id
      case None => 1
    }
  }

}
