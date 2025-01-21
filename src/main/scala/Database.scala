import java.sql.{Connection, DriverManager, ResultSet}

object Database {
  val url = "jdbc:sqlite:game_scores.db"

  def connect(): Connection = DriverManager.getConnection(url)

  def initialize(): Unit = {
    val connection = connect()
    val statement = connection.createStatement()
    statement.execute(
      """
        |CREATE TABLE IF NOT EXISTS scores (
        |  id INTEGER PRIMARY KEY,
        |  game TEXT NOT NULL,
        |  level TEXT NOT NULL,
        |  name TEXT NOT NULL,
        |  time INTEGER NOT NULL
        |)
      """.stripMargin)
    statement.close()
    connection.close()
  }

  def saveScore(game: String, level:String, name: String, time: Int): Unit = {
    val connection = connect()
    val statement = connection.prepareStatement("INSERT INTO scores (game, level, name, time) VALUES (?, ?, ?, ?)")
    statement.setString(1, game)
    statement.setString(2, level)
    statement.setString(3, name)
    statement.setInt(4, time)
    statement.executeUpdate()
    statement.close()
    connection.close()
  }

  def getScores(game: String, level: String): Seq[(String, Int)] = {
    val connection = connect()
    val statement = connection.prepareStatement(
      "SELECT name, time FROM scores WHERE game = ? AND level = ? ORDER BY time ASC"
    )
    statement.setString(1, game)
    statement.setString(2, level)
    val resultSet = statement.executeQuery()
    val scores = Iterator.continually((resultSet.next(), resultSet)).takeWhile(_._1).map {
      case (_, rs) => (rs.getString("name"), rs.getInt("time"))
    }.toList
    resultSet.close()
    statement.close()
    connection.close()
    scores
  }
}
