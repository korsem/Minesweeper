import scalafx.scene.layout.GridPane
import scalafx.scene.control.TextField
import scalafx.scene.input.{MouseEvent, MouseButton}
import scalafx.scene.paint.Color
import scalafx.Includes.*

object SudokuBoard {
  // current step - drawing the board

  case class Cell(value: Option[Int], row: Int, col: Int, controller: GameController, isEditable: Boolean = true) extends TextField {
    // other characters than max 1 to 9 are not allowed
    prefWidth = 50
    prefHeight = 50
    style = "-fx-font-size: 18; -fx-alignment: center;"
    editable = isEditable

    // TODO: not editable cells should have bold font
    // TODO: handle the situation when user wants to add more than 1 number

    value.foreach(v => text = v.toString)

    this.onMouseClicked = (e: MouseEvent) => {
      e.button match {
        case MouseButton.Primary => handleLeftClick()
        case MouseButton.Middle => handleMiddleClick()
        case MouseButton.Secondary => handleRightClick()
        case _ => // Ignore other mouse buttons
      }
    }

    def handleLeftClick(): Unit = {
      // input a number taken into account in solution
      if (isEditable) {
        text = ""
        requestFocus()
        text.onChange { (_, _, newValue) =>
          if (!newValue.matches("[1-9]?")) {
            text = "" // Clear if invalid input
          }
        }
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

  type Board = Vector[Vector[Option[Int]]]

  private var stopTimer: () => Int = () => 0

  def setTimerStopper(stopper: () => Int): Unit = {
    stopTimer = stopper
  }

  def generateBoard(): Board = {
    // TODO: generate a valid sudoku board => add the logic later
    Vector.fill(9, 9)(None)
  }

  def renderBoard(board: Board, controller: GameController): GridPane = {
    // sudoku board consisation of 9x9 cells
    // i want the border to be thicker every 3 cells
    val gridPane = new GridPane()

    for (row <- 0 until 9; col <- 0 until 9) {
      val cell = Cell(board(row)(col), row, col, controller)
      gridPane.add(cell, col, row)

      val borderStyle = new StringBuilder()
      if (col % 3 == 2 && col != 8) {
        borderStyle.append("-fx-border-right-width: 2; -fx-border-right-color: black;")
      }
      if (row % 3 == 2 && row != 8) {
        borderStyle.append("-fx-border-bottom-width: 2; -fx-border-bottom-color: black;")
      }
      cell.style = cell.style.value + borderStyle.toString()
    }

    gridPane
  }
}

// TODO:
// logic - next step filling valid numbers which cannot be changed
// the board is generated randomly
// the board is solvable
// the board has only one solution