package jp.nukokusabot

import org.scalatest._
import java.sql._

class SQLiteTest extends FlatSpec with BeforeAndAfter {

  Class.forName("org.sqlite.JDBC")
  val dbPath = "jdbc:sqlite:" + getClass.getResource("/for_test.db").getPath
  val conn = DriverManager.getConnection(dbPath)
  val stmt = conn.createStatement

  before {
    stmt.executeUpdate("create table if not exists test_table(id integer primary key autoincrement, name text, age integer)")
    stmt.executeUpdate("insert into test_table values (0, 'alice', 20)")
  }

  "SQLite Driver" should "get the row" in {
    val resultSet = stmt.executeQuery("select * from test_table")

    while (resultSet.next()) {
      assert(resultSet.getString("name") === "alice")
    }
  }

  it should "throw error for ununique id" in {
    intercept[SQLException] {
      stmt.executeQuery("insert into test_table values(0, 'bob', 25)")
    }
  }

  it should "confirm that the table is exist" in {
    val select = "SELECT COUNT(*) AS count FROM sqlite_master WHERE type='table' AND name='test_table'"
    val row = stmt.executeQuery(select)

    assert(row.getInt("count") === 1)
  }

  it should "increment id value automatically" in {
    stmt.executeUpdate("INSERT INTO test_table(name, age) VALUES('bob', 25)")
    val rs = stmt.executeQuery("SELECT * FROM test_table WHERE name='bob'")

    assert(rs.getInt("id") === 1)
    assert(rs.getInt("age") === 25)
  }

  it should "confirm the record is exist or not easily" in {
    assert(stmt.executeQuery("SELECT * FROM test_table WHERE name='alice'").next === true)
    assert(stmt.executeQuery("SELECT * FROM test_table WHERE name='deen'").next  === false)
  }

  it should "increment integer column" in {
    stmt.executeUpdate("UPDATE test_table SET age = age+1 WHERE name='alice'")
    val rs = stmt.executeQuery("SELECT * FROM test_table WHERE name='alice'")

    assert(rs.getInt("age") === 21)
  }

  after {
    stmt.executeUpdate("DROP TABLE test_table")
    stmt.close
  }

}
