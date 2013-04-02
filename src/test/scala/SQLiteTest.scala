package jp.nukokusabot

import org.scalatest._
import java.sql._

class SQLiteTest extends FlatSpec with BeforeAndAfter {

  Class.forName("org.sqlite.JDBC")
  val dbPath = "jdbc:sqlite:" + getClass.getResource("/for_test.db").getPath
  val conn = DriverManager.getConnection(dbPath)
  val stmt = conn.createStatement

  before {
    stmt.executeUpdate("create table if not exists test_table(id integer primary key, name text)")
    stmt.executeUpdate("insert into test_table values (0, 'hey')")
  }

  "SQLite Driver" should "get the row" in {
    val resultSet = stmt.executeQuery("select * from test_table")

    while (resultSet.next()) {
      assert(resultSet.getString("name") === "hey")
    }
  }

  it should "throw error for ununique id" in {
    intercept[SQLException] {
      stmt.executeQuery("insert into test_table values(0, 'hey')")
    }
  }

  it should "confirm that the table is exist" in {
    val select = "SELECT COUNT(*) AS count FROM sqlite_master WHERE type='table' AND name='test_table'"
    val row = stmt.executeQuery(select)

    assert(row.getInt("count") === 1)
  }

  after {
    stmt.executeUpdate("DROP TABLE test_table")
    stmt.close
  }

}
