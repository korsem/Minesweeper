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
          case MouseButton.Primary => handleLeftClick(button, board(row)(col), row, col, board)
          case MouseButton.Secondary => handleRightClick(button)
          case _ => // Ignorujemy inne przyciski
        }
      }

      grid.add(button, col, row)
    }

    grid
  }

  def handleLeftClick(button: Button, cell: Cell, row: Int, col: Int, board: Board): Unit = {
    cell match {
      case Mine => button.text = "💣"
      case Empty => button.text = GameLogic.countAdjacentMines(board, row, col).toString
    }
  }

  def handleRightClick(button: Button): Unit = {
    button.text = if (button.text.equals("🚩")) "" else "🚩"
  }
}