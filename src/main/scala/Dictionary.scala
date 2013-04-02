package jp.nukokusabot

import java.sql.{Array => _, _}
import net.reduls.gomoku._

object Dictionary {

  private var conn: Connection = _

  def initialize(dbFile: String) {
    Class.forName("org.sqlite.JDBC")
    conn = DriverManager.getConnection("jdbc:sqlite:" + getClass.getResource("/" + dbFile).getPath)
    val stmt = conn.createStatement

    if (!isTableExist("dictionary")) {
      stmt.executeUpdate("CREATE TABLE dictionary (id INTEGER PRIMARY KEY, surface TEXT, feature TEXT)")
      stmt.executeUpdate("INSERT INTO dictionary VALUES(0, '', head)")
      stmt.executeUpdate("INSERT INTO dictionary VALUES(1, '', tail)")
    }
    if (!isTableExist("link")) {
      stmt.executeUpdate("CREATE TABLE link (pre_id INTEGER, suc_id INTEGER, times INTEGER, PRIMARY KEY(pre_id, suc_id))")
    }

    stmt.close
  }

  def appendWord(word: Word): Unit = {}

  def appendLink(pre: Int, suc:Int): Unit = {}

  private def isTableExist(table: String): Boolean = {
    val stmt = conn.createStatement
    val select = "SELECT COUNT(*) AS count FROM sqlite_master WHERE type='table' AND name="

    stmt.executeQuery(select + "'" + table + "'").getInt("count") > 0
  }

  private def isWordRegisterd(word: Word): Boolean = { true }

  private def isLinkRegisterd(pre: Int, suc: Int): Boolean ={ true }
}
