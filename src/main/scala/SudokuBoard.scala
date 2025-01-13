import scalafx.scene.layout.GridPane
import scalafx.scene.control.TextField
import scalafx.scene.input.{MouseEvent, MouseButton}
import scalafx.scene.paint.Color
import scalafx.Includes.*

object SudokuBoard {
  case class Cell(value: Option[Int], row: Int, col: Int, controller: GameController, isEditable: Boolean = true) extends TextField {
    prefWidth = 50
    prefHeight = 50
    style = "-fx-font-size: 18; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 1;"
    editable = isEditable

    value.foreach(v => text = v.toString)

    this.onMouseClicked = (e: MouseEvent) => {
      e.button match {
        case MouseButton.Primary => handleLeftClick()
        case MouseButton.Middle => handleMiddleClick()
        case MouseButton.Secondary => handleRightClick()
        case _ => // Ignore other mouse buttons
      }
// not workiung -     highlightRowAndColumn(parent.value.asInstanceOf[javafx.scene.layout.GridPane])
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
        style = "-fx-font-size: 14; -fx-text-fill: black;"
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

    private def highlightRowAndColumn(gridPane: javafx.scene.layout.GridPane): Unit = {
      
      // something wrong with types - Cannot invoke "SudokuBoard$Cell.style()" because "rowCell" is null
      for (i <- 0 until 9) {
        val rowCell = gridPane.lookup(s"#cell-$row-$i").asInstanceOf[Cell]
        val colCell = gridPane.lookup(s"#cell-$i-$col").asInstanceOf[Cell]

        rowCell.style = rowCell.style.value + "; -fx-background-color: lightyellow;"
        colCell.style = colCell.style.value + "; -fx-background-color: lightyellow;"
      }
    }
  }

  type Board = Vector[Vector[Option[Int]]]
  
  // wywolane validBoard - def boardFill()

  private var stopTimer: () => Int = () => 0

  val validBoard: Vector[Vector[Int]] = SudokuLogic.generateValidBoard()

  def setTimerStopper(stopper: () => Int): Unit = {
    stopTimer = stopper
  }

  def generateBoard(): Board = {
    // generate play board based on validBoard - consisting of cells not int wioth some empty cells (to fill for the player)
    // it should be done so it is possible to solve the board and there is only 1 way to solve it
    // I may create a method to check if the board is solvable in GameLogic
    validBoard.map(row => row.map(Some(_)))
    
  }

  def renderBoard(board: Board, controller: GameController): GridPane = {
    val gridPane = new GridPane()

    for (row <- 0 until 9; col <- 0 until 9) {
      val cell = Cell(board(row)(col), row, col, controller)
      gridPane.add(cell, col, row)

      val borderStyle = new StringBuilder()
      
      // 3x3 subgrid borders: thicker every 3 rows and columns
//      borderStyle.append("-fx-border-right-width: 1; -fx-border-right-color: black;")
//      borderStyle.append("-fx-border-bottom-width: 1; -fx-border-bottom-color: black;")

      cell.style = cell.style.value + borderStyle.toString()
    }

    gridPane
  }
}
