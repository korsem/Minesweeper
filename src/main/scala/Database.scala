import java.sql.{Connection, DriverManager, ResultSet}

object Database {
  // Database.scala jest odpowiedzialne za obsługę bazy danych SQLite, w której przechowywane są wyniki gier.
  val url = "jdbc:sqlite:game_scores.db"

  def connect(): Connection = DriverManager.getConnection(url)
  // Metoda connect() tworzy połączenie z bazą danych SQLite

  def initialize(): Unit = {
    // Metoda initialize() tworzy tabelę scores w bazie danych, jeśli nie istnieje.
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

  // Metoda saveScore() zapisuje wynik gry w bazie danych.
  // wynik się zapisuję dla danej gry, poziomu trudności, nazwy gracza i czasu gry.
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

  // Metoda getScores() pobiera wyniki gry z bazy danych.
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
