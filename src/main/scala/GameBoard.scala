import scalafx.Includes.*
import scalafx.scene.Scene
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

  def renderBoard(board: Board, controller: GameController): GridPane = {
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
          case MouseButton.Primary => handleLeftClick(button, board(row)(col), row, col, board, revealed, buttons, controller)
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

  def handleLeftClick(button: Button, cell: Cell, row: Int, col: Int, board: Board, revealed: Array[Array[Boolean]], buttons: Array[Array[Button]], controller: GameController): Unit = {
    if (revealed(row)(col)) return // If the cell is already revealed, do nothing

    cell match {
      case Mine =>
        button.text = "💣"
        button.style = "-fx-base: red"
        //      // wait for 2 seconds and show the game over screen
        //      val timeline = new Timeline {
        //        cycleCount = 1
        //        keyFrames = Seq(
        //          KeyFrame(Duration(2000), _ => {
        //            controller.getStage.scene = new Scene {
        //              root = new GameOverMinesweeperView(controller, board.length, board.head.length, board.flatten.count(_ == Mine))
        //            }
        //          })
        //        )
        //      }
        //      timeline.play()

        controller.getStage.scene = new Scene {
          root = new GameOverMinesweeperView(controller, board.length, board.head.length, board.flatten.count(_ == Mine))
        }
      case Empty =>
        val numberOfAdjacentMines = GameLogic.countAdjacentMines(board, row, col)
        if (numberOfAdjacentMines > 0) {
          button.text = numberOfAdjacentMines.toString
          button.style = "-fx-base: lightgray"
          revealed(row)(col) = true
        } else {
          // flood fill revealing empty cells
          val cellsToReveal = GameLogic.floodFill(board, row, col)
          // reveal empty cells with no adjacent Mines
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
    // mozna dodać licznik ile zostało jeszcze flag (tyle co min)
    button.text = if (button.text.equals("🚩")) "" else "🚩"
    // okienko wygraleś - po nim sie wyświetla imie do wpisania i wynik,
    // ale zanim się wyświetli to przez 2 sekundy wyświetla się odsłonięta plansza z bombami
  }
}