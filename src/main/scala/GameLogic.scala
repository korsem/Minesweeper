import scalafx.Includes.*
// Mechanika gry:
//Zliczanie liczby min wokół danej komórki. checked
//Sprawdzanie warunków wygranej/przegranej.
//Automatyczne odsłanianie pustych obszarów (tzw. flood fill).
object GameLogic {
  def isValidCell(board: GameBoard.Board, row: Int, col: Int): Boolean = {
    // method checks if the cell is within the board
    row >= 0 && row < board.length && col >= 0 && col < board.head.length
  }

  def countAdjacentMines(board: GameBoard.Board, row: Int, col: Int): Int = {
    // method counts the number of mines around a given cell
    val rows = board.length
    val cols = board.head.length
    val deltas = for {
      dx <- -1 to 1
      dy <- -1 to 1
      if dx != 0 || dy != 0
    } yield (dx, dy)
    deltas.count { case (dx, dy) =>
      val newRow = row + dx
      val newCol = col + dy
      // checks if neighboring cell is a mine
      isValidCell(board, newRow, newCol) && board(newRow)(newCol) == GameBoard.Mine
    }
  }
}
