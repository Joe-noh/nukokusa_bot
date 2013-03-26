package jp.nukokusabot

import org.scalatest._
import java.sql._

class SQLiteTest extends FlatSpec with BeforeAndAfter {

  Class.forName("org.sqlite.JDBC")
  val dbPath = "jdbc:sqlite:" + getClass.getResource("/for_test.db").getPath//.replace("/", ":")
  val connection = DriverManager.getConnection(dbPath)

  before {
    val statement = connection.createStatement
    statement.executeUpdate("create table if not exists test_table(id, name)")
    statement.executeUpdate("insert into test_table values (1, 'hey')")
    statement.close
  }

  "SQLite Driver" should "get the row" in {
    val statement = connection.createStatement
    val resultSet = statement.executeQuery("select * from test_table")

    while (resultSet.next()) {
      assert(resultSet.getString("name") === "hey")
    }
  }

  after {
    val statement = connection.createStatement
    statement.executeUpdate("DROP TABLE test_table")
  }

}
