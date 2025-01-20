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

    // nie działa jeśli zaczne od razu wpisywać liczbę
    def handleLeftClick(): Unit = {
      if (isEditable) {
        requestFocus()
        text.onChange { (_, oldValue, newValue) =>
          if (newValue.matches("[1-9]?")) {
            val intValue = if (newValue.isEmpty) 0 else newValue.toInt
            SudokuBoard.userBoard = SudokuBoard.userBoard.updated(row, SudokuBoard.userBoard(row).updated(col, intValue))
          } else {
            println("Invalid input")
          }
        }
        style = "-fx-font-size: 14; -fx-text-fill: darkblue;"
      }
    }

    def handleMiddleClick(): Unit = {
      if (isEditable) {
        text = ""
        SudokuBoard.userBoard = SudokuBoard.userBoard.updated(row, SudokuBoard.userBoard(row).updated(col, 0))
      }
    }

    def handleRightClick(): Unit = {
      if (isEditable) {
        text = ""
        style = "-fx-font-size: 14; -fx-text-fill: gray;"
        SudokuBoard.userBoard = SudokuBoard.userBoard.updated(row, SudokuBoard.userBoard(row).updated(col, 0))
      }
    }
  }

  type Board = Vector[Vector[Cell]]

  private def convertToBoard(intBoard: Vector[Vector[Int]], controller: GameController): Board = {
    intBoard.zipWithIndex.map { case (row, rowIndex) =>
      row.zipWithIndex.map { case (value, colIndex) =>
        Cell(if (value == 0) None else Some(value), rowIndex, colIndex, controller, value == 0)
      }
    }
  }

  private var stopTimer: () => Int = () => 0

  def setTimerStopper(stopper: () => Int): Unit = {
    stopTimer = stopper
  }

  // valid Board and its equivalent initial board, but all the value are filled
  private val validBoard: Vector[Vector[Int]] = SudokuLogic.generateValidBoard()
  private var initialBoard: Vector[Vector[Int]] = _ // validBoard but with 0 values where empty cells after generating board
  var userBoard: Vector[Vector[Int]] = _ // user's board

  def generateBoard(lvl: Int, controller: GameController): Board = {
    val random = new Random()
    val filledCells = lvl match {
      case 1 => 70 // Easy: 70 cells filled
      case 2 => 38 // Medium: 38 cells filled
      case 3 => 25 // Hard: 25 cells filled
    }

    var board = validBoard.map(_.map(Option(_))) // convert to Vector[Vector[Option[Int]]]

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

    // store the initial state of the board, if None store as 0
    initialBoard = validBoard.zipWithIndex.map { case (row, rowIndex) =>
      row.zipWithIndex.map { case (value, colIndex) =>
        if (board(rowIndex)(colIndex).isEmpty) 0 else value
      }
    }
    // the current board state
    userBoard = initialBoard.map(_.map(identity))

    convertToBoard(board.map(_.map(_.getOrElse(0))), controller)
  }

  def boardToInitial(board: Board, controller: GameController): Board = {
    convertToBoard(initialBoard, controller)
  }

  def boardToValid(board: Board, controller: GameController): Board = {
    convertToBoard(validBoard, controller)
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

  def checkUserBoard(): Seq[(Int, Int)] = {
    // Check if the user board is correct, return incorrect cells, or empty if correct
    val incorrectCells = for {
      row <- 0 until 9
      col <- 0 until 9
      if {
        println(s"Checking cell ($row, $col): userBoard=${userBoard(row)(col)}, validBoard=${validBoard(row)(col)}")
        userBoard(row)(col) != validBoard(row)(col)
      }
    } yield (row, col)

    incorrectCells
  }
}