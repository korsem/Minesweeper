import scalafx.scene.layout.GridPane
import scalafx.scene.control.TextField
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.Includes._

import scala.util.Random

object SudokuBoard {
  case class Cell(value: Option[Int], row: Int, col: Int, controller: GameController, isEditable: Boolean = true) extends TextField {
    prefWidth = 50
    prefHeight = 50
    editable = isEditable
    style = if (isEditable) "-fx-font-size: 18; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 1;"
    else "-fx-font-size: 18; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 1; -fx-font-weight: bold;"

    value.foreach(v => text = v.toString)
    id = s"cell-$row-$col" // Set the unique id for each cell
    
    def changeBackgroundColor(color: String): Unit = {
      style = style.value + s"; -fx-background-color: $color;"
    }

    def thickenRightBorder(): Unit = {
      style = style.value + "; -fx-border-right-width: 3;"
    }

    def thickenLeftBorder(): Unit = {
      style = style.value + "; -fx-border-left-width: 3;"
    }

    def thickenTopBorder(): Unit = {
      style = style.value + "; -fx-border-top-width: 3;"
    }

    def thickenBottomBorder(): Unit = {
      style = style.value + "; -fx-border-bottom-width: 3;"
    }

    this.onMouseClicked = (e: MouseEvent) => {
      e.button match {
        case MouseButton.Primary => handleLeftClick()
        case MouseButton.Middle => handleMiddleClick()
        case MouseButton.Secondary => handleRightClick()
        case _ => // Ignore other mouse buttons
      }
    }

    def handleLeftClick(): Unit = {
      if (isEditable) {
        text = ""
        requestFocus()
        text.onChange { (_, _, newValue) =>
          if (!newValue.matches("[1-9]?")) {
            text = "" // Clear if invalid input
          }
        }
        style = "-fx-font-size: 14; -fx-text-fill: darkblue;"
      }
    }

    def handleMiddleClick(): Unit = {
      // used to clear the text
      if (isEditable) {
        text = ""
      }
    }

    def handleRightClick(): Unit = {
      // to add a number which is not taken into account in the solution,
      // and is cleared with a left click as well as the middle click
      if (isEditable) {
        text = ""
        style = "-fx-font-size: 14; -fx-text-fill: gray;"
      }
    }
  }

  type Board = Vector[Vector[Cell]]

  private var stopTimer: () => Int = () => 0

  def setTimerStopper(stopper: () => Int): Unit = {
    stopTimer = stopper
  }

  val validBoard: Vector[Vector[Int]] = SudokuLogic.generateValidBoard()

  def generateBoard(lvl: Int, controller: GameController, initial: Boolean): Board = {
    val random = new Random()
    val filledCells = lvl match {
      case 1 => 70 // Easy: 70 cells filled
      case 2 => 38 // Medium: 38 cells filled
      case 3 => 25 // Hard: 25 cells filled
    }

    var board = validBoard.map(_.map(Option(_)))

    if (initial) {
      // Reset the existing board by clearing user-editable cells
      board = board.map(row => row.map {
        case Some(value) if value != 0 => Some(value)
        case _ => None
      })
    } else {
      // Generate a new board by removing cells
      for (_ <- 0 until (81 - filledCells)) {
        var row = 0
        var col = 0
        while (board(row)(col).isEmpty) {
          row = random.nextInt(9)
          col = random.nextInt(9)
        }
        board = board.updated(row, board(row).updated(col, None))
      }
    }

    board.zipWithIndex.map { case (row, rowIndex) =>
      row.zipWithIndex.map { case (value, colIndex) =>
        Cell(value, rowIndex, colIndex, controller, value.isEmpty)
      }
    }
  }

  def renderBoard(board: Board, controller: GameController): GridPane = {
    val gridPane = new GridPane()

    for (row <- 0 until 9; col <- 0 until 9) {
      val cell = board(row)(col)
      gridPane.add(cell, col, row)

      if (row % 3 == 0) {
        cell.thickenTopBorder()
      }
      if (col % 3 == 0) {
        cell.thickenLeftBorder()
      }
      if ((row + 1) % 3 == 0) {
        cell.thickenBottomBorder()
      }
      if ((col + 1) % 3 == 0) {
        cell.thickenRightBorder()
      }
    }

    gridPane
  }
  
def checkUserBoard(userBoard: Board): Seq[(Int, Int)] = {
  // Check if the user board is correct, return incorrect cells, or empty if correct
  // prztyrównaj do valid board, chce jeszcze printowac te wartości w celu znalezienia błędu
  print(userBoard)
  validBoard.zip(userBoard).zipWithIndex.flatMap { case ((validRow, userRow), row) =>
    validRow.zip(userRow).zipWithIndex.flatMap { case ((validValue, userCell), col) =>
      if (userCell.value.contains(validValue)) {
        None
      } else {
        Some((row, col))
      }
    }
  }
}
}
