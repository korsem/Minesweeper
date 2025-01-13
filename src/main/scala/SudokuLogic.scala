import scala.util.Random
import scalafx.Includes.*

object SudokuLogic {

  def generateValidBoard(): Vector[Vector[Int]] = {
    val board = Array.ofDim[Int](9, 9)
    fillBoard(board)
    board.map(_.toVector).toVector
  }

  private def fillBoard(board: Array[Array[Int]]): Boolean = {
    // methods fills the board with random numbers, returns true if the board is filled
    val emptyCell = findEmptyCell(board)
    if (emptyCell.isEmpty) return true // there are no empty cells left

    val (row, col) = emptyCell.get
    val numbers = Random.shuffle((1 to 9).toList)

    for (num <- numbers) {
      if (isValid(board, row, col, num)) {
        board(row)(col) = num
        if (fillBoard(board)) 
          return true
        board(row)(col) = 0
      }
    }
    false
  }

  private def findEmptyCell(board: Array[Array[Int]]): Option[(Int, Int)] = {
    // returns the coordinates of the first empty cell
    for (row <- 0 until 9; col <- 0 until 9) {
      if (board(row)(col) == 0) 
        return Some((row, col))
    }
    None
  }

  private def isValid(board: Array[Array[Int]], row: Int, col: Int, num: Int): Boolean = {
    // check if the number is not in the same row, column or subgrid - sudoku condition
    !board(row).contains(num) &&
      !board.map(_(col)).contains(num) &&
      !getSubGrid(board, row, col).contains(num)
  }

  private def getSubGrid(board: Array[Array[Int]], row: Int, col: Int): Seq[Int] = {
    // get the 3x3 subgrid of the given cell
    val subGridRow = (row / 3) * 3
    val subGridCol = (col / 3) * 3
    for {
      r <- subGridRow until subGridRow + 3
      c <- subGridCol until subGridCol + 3
    } yield board(r)(c)
  }
}