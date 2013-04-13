package jp.nukokusabot

import scala.util.Random
import java.sql.{Array => _, _}
import net.reduls.gomoku._

object Dictionary extends Extends {

  private var conn: Connection = _

  def initialize(dbFile: String): Unit = {
    Class.forName("org.sqlite.JDBC")
    conn = DriverManager.getConnection("jdbc:sqlite:" + getClass.getResource("/" + dbFile).getPath)
    val stmt = conn.createStatement

    if (!isTableExist("dictionary")) {
      stmt.executeUpdate("CREATE TABLE dictionary (id INTEGER PRIMARY KEY AUTOINCREMENT, surface TEXT, feature TEXT)")
      stmt.executeUpdate("INSERT INTO dictionary VALUES(0, '', head)")
      stmt.executeUpdate("INSERT INTO dictionary VALUES(1, '', tail)")
    }
    if (!isTableExist("link")) {
      stmt.executeUpdate("CREATE TABLE link (pre_id INTEGER, suc_id INTEGER, times INTEGER, PRIMARY KEY(pre_id, suc_id))")
    }

    stmt.close
  }

  def fetchNextID(id: Int): Int = {
    def listApplicants(rows: ResultSet, acc: List[Int]): List[Int] = rows.next match {
      case true  => listApplicants(rows, List(rows.getInt("suc_id")) * rows.getInt("times"))
      case false => acc
    }

    val stmt = conn.prepareStatement("SELECT * FROM link WHERE pre_id=?")
    stmt.setInt(1, id)
    val rows = stmt.executeQuery

    val applicants = listApplicants(rows, List[Int]())
    stmt.close

    val rand = new Random
    return applicants(rand.nextInt(applicants.length))
  }

  def randomChoice: (Int, String) = {
    val stmt = conn.createStatement
    val rows = stmt.executeQuery("SELECT * FROM dictionary ORDER BY RANDOM() LIMIT 1")
    (rows.getInt("id"), rows.getString("surface"))
  }

  def registerWords(words: List[Word]): Unit = {
    val stmt = conn.prepareStatement("INSERT INTO dictionary(surface, feature) VALUES(?, ?)")
    for (word <- words) {
      if (!isWordRegistered(word)) {
        stmt.setString(1, word.surface)
        stmt.setString(2, word.feature)
        stmt.executeUpdate
      }
    }
    stmt.close
  }

  def translateToJapanese(idList: List[Int]): String = {
    def buildString(rows: ResultSet, acc: String): String = rows.next match {
      case true  => buildString(rows, rows.getString("surface"))
      case false => acc
    }

    val ids = idList.mkString("", ",", "")
    val stmt = conn.createStatement
    val rows = stmt.executeQuery("SELECT surface FROM dictionary WHERE id IN ("+ids+") ORDER BY dictionary.id IN ("+ids+")")
    stmt.close

    buildString(rows, "")
  }

  def scrap = {
    val stmt = conn.createStatement
    stmt.executeUpdate("DROP TABLE dictionary")
    stmt.executeUpdate("DROP TABLE link")
    stmt.close
  }

  def registerLinks(words: List[Word]): Unit = {
    val idList = (0 +: words.map(fetchID) :+ 1).sliding(2)
    for (List(pre, suc) <- idList) {
      if (isLinkRegistered(pre, suc)) {
        incrementLinkTime(pre, suc)
      } else {
        registerNewLink(pre, suc)
      }
    }
  }

  private def fetchID(word: Word): Int = {
    val stmt = conn.prepareStatement("SELECT id FROM dictionary WHERE surface=? AND feature=?")
    stmt.setString(1, word.surface)
    stmt.setString(2, word.feature)
    val id = stmt.executeQuery.getInt("id")

    stmt.close
    return id
  }

  private def incrementLinkTime(pre: Int, suc: Int): Unit = {
    val stmt = conn.prepareStatement("UPDATE link SET times = times+1 WHERE pre_id=? AND suc_id=?")
    stmt.setInt(1, pre)
    stmt.setInt(2, suc)
    stmt.executeUpdate
    stmt.close
  }

  private def registerNewLink(pre: Int, suc:Int): Unit = {
    val stmt = conn.prepareStatement("INSERT INTO link VALUES(?, ?, 1)")
    stmt.setInt(1, pre)
    stmt.setInt(2, suc)
    stmt.executeUpdate
    stmt.close
  }

  private def isTableExist(table: String): Boolean = {
    val stmt = conn.prepareStatement("SELECT * FROM sqlite_master WHERE type='table' AND name=?")
    stmt.setString(1, table)
    val exist = stmt.executeQuery.next
    stmt.close

    return exist
  }

  private def isWordRegistered(word: Word): Boolean = {
    val stmt = conn.prepareStatement("SELECT * FROM dictionary WHERE surface=? AND feature=?")
    stmt.setString(1, word.surface)
    stmt.setString(2, word.feature)
    val registered = stmt.executeQuery.next
    stmt.close

    return registered
  }

  private def isLinkRegistered(pre: Int, suc: Int): Boolean = {
    val stmt = conn.prepareStatement("SELECT * FROM link WHERE pre_id=? AND suc_id=?")
    stmt.setInt(1, pre)
    stmt.setInt(2, suc)
    val registered = stmt.executeQuery.next
    stmt.close

    return registered
  }
}
