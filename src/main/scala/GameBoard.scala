import scalafx.Includes.*
import scalafx.scene.layout.GridPane
import scalafx.scene.control.Button
import scalafx.stage.Stage
import scalafx.scene.input.{MouseButton, MouseEvent}

object GameBoard {
  sealed trait Cell

  case object Mine extends Cell

  case object Empty extends Cell

  type Board = Vector[Vector[Cell]]

  def generateBoard(rows: Int, cols: Int, mines: Int): Board = {
    val emptyCells = Vector.fill(rows * cols - mines)(Empty)
    val mineCells = Vector.fill(mines)(Mine)
    val shuffled = scala.util.Random.shuffle(emptyCells ++ mineCells)
    shuffled.grouped(cols).toVector
  }

  def renderBoard(board: Board, stage: Stage, backToMenu: () => Unit): GridPane = {
    val grid = new GridPane()
    val revealed = Array.fill(board.length, board.head.length)(false)
    val buttons = Array.ofDim[Button](board.length, board.head.length)

    for {
      row <- board.indices
      col <- board.head.indices
    } {
      // button creation
      val button = new Button {
        prefWidth = 50
        prefHeight = 50
      }

      // mouse event handler
      button.onMouseClicked = (e: MouseEvent) => {
        e.button match {
          case MouseButton.Primary => handleLeftClick(button, board(row)(col), row, col, board, revealed, buttons)
          case MouseButton.Secondary => handleRightClick(button)
          case _ => // Ignorujemy inne przyciski
        }
      }

      // Dodanie przycisku do siatki i tablicy
      buttons(row)(col) = button
      grid.add(button, col, row)
    }

    grid
  }

  def handleLeftClick(button: Button, cell: Cell, row: Int, col: Int, board: Board, revealed: Array[Array[Boolean]], buttons: Array[Array[Button]]): Unit = {
    if (revealed(row)(col)) return // If the cell is already revealed, do nothing

    cell match {
      case Mine =>
        button.text = "💣"
        button.style = "-fx-base: red"
        println("Przegrana!") // TODO: Add a game over window
      case Empty =>
        val numberOfAdjacentMines = GameLogic.countAdjacentMines(board, row, col)
        if (numberOfAdjacentMines > 0) {
          button.text = numberOfAdjacentMines.toString
          button.style = "-fx-base: lightgray"
          revealed(row)(col) = true
        } else {
          // flood fill reaviling empty cells
          val cellsToReveal = GameLogic.floodFill(board, row, col)
          // reveal empty cells with no adjeacent Mines
          cellsToReveal.foreach { case (r, c) =>
            if (!revealed(r)(c)) {
              revealed(r)(c) = true
              val adjacentMines = GameLogic.countAdjacentMines(board, r, c)
              buttons(r)(c).text = if (adjacentMines > 0) adjacentMines.toString else ""
              buttons(r)(c).style = "-fx-base: lightgray"
            }
          }
          // reveal external cells with number of adjacent mines
          val externalCells = GameLogic.externalCells(board, cellsToReveal)
          externalCells.foreach { case (r, c) =>
            if (!revealed(r)(c)) {
              revealed(r)(c) = true
              val adjacentMines = GameLogic.countAdjacentMines(board, r, c)
              buttons(r)(c).text = if (adjacentMines > 0) adjacentMines.toString else ""
              buttons(r)(c).style = "-fx-base: lightgray"
            }
          }
          
        }
    }
  }

  def handleRightClick(button: Button): Unit = {
    // trzeba jescze sprawdzić czy nie jest odkryte, bo jak jest to nie można zmienić
    button.text = if (button.text.equals("🚩")) "" else "🚩"
  }
}