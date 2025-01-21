import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration
import scalafx.scene.Scene
import scalafx.scene.layout.GridPane
import scalafx.scene.control.Button
import scalafx.stage.Stage
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.Includes.*

object GameBoard {
  sealed trait Cell
  case object Mine extends Cell
  case object Empty extends Cell

  type Board = Vector[Vector[Cell]]

  var loose = false
  var flagCount = 0
  var buttons: Array[Array[Button]] = _
  private var updateFlagCount: () => Unit = () => {}
  private var stopTimer: () => Int = () => 0

  def setFlagCountUpdater(updater: () => Unit): Unit = {
    updateFlagCount = updater
  }

  def setTimerStopper(stopper: () => Int): Unit = {
    stopTimer = stopper
  }

  def generateBoard(rows: Int, cols: Int, mines: Int): Board = {
    flagCount = mines
    val emptyCells = Vector.fill(rows * cols - mines)(Empty)
    val mineCells = Vector.fill(mines)(Mine)
    val shuffled = scala.util.Random.shuffle(emptyCells ++ mineCells)
    shuffled.grouped(cols).toVector
  }

  def renderBoard(board: Board, controller: GameController): GridPane = {
    val grid = new GridPane()
    val revealed = Array.fill(board.length, board.head.length)(false)
    buttons = Array.ofDim[Button](board.length, board.head.length)

    for {
      row <- board.indices
      col <- board.head.indices
    } {
      val button = new Button {
        prefWidth = if (board.length == 9) 50 else 37
        prefHeight = if (board.length == 9) 50 else 38
      }

      button.onMouseClicked = (e: MouseEvent) => {
        e.button match {
          case MouseButton.Primary => handleLeftClick(button, board(row)(col), row, col, board, revealed, buttons, controller)
          case MouseButton.Secondary => handleRightClick(button, revealed(row)(col), board, controller)
          case _ => 
        }
      }

      buttons(row)(col) = button
      grid.add(button, col, row)
    }

    grid
  }

  def handleLeftClick(button: Button, cell: Cell, row: Int, col: Int, board: Board, revealed: Array[Array[Boolean]], buttons: Array[Array[Button]], controller: GameController): Unit = {
    if (revealed(row)(col)) return

    cell match {
      case Mine =>
        button.text = "💣"
        button.style = "-fx-base: red"
        revealed(row)(col) = true
        loose = true
        val timeline = new Timeline {
          cycleCount = 1
          keyFrames = Seq(
            KeyFrame(Duration(2000), onFinished = _ => {
              controller.getStage.scene = new Scene {
                root = new GameOverMinesweeperView(controller, board.length, board.head.length, board.flatten.count(_ == Mine))
              }
            })
          )
        }
        timeline.play()

      case Empty =>
        val numberOfAdjacentMines = GameLogic.countAdjacentMines(board, row, col)
        if (numberOfAdjacentMines > 0) {
          button.text = numberOfAdjacentMines.toString
          button.style = "-fx-base: lightgray"
          revealed(row)(col) = true
        } else {
          val cellsToReveal = GameLogic.floodFill(board, row, col)
          cellsToReveal.foreach { case (r, c) =>
            if (!revealed(r)(c)) {
              revealed(r)(c) = true
              val adjacentMines = GameLogic.countAdjacentMines(board, r, c)
              buttons(r)(c).text = if (adjacentMines > 0) adjacentMines.toString else ""
              buttons(r)(c).style = "-fx-base: lightgray"
            }
          }
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

  def handleRightClick(button: Button, isRevealed: Boolean, board: Board, controller: GameController): Unit = {
    if (isRevealed) return

    if (button.text.value == "🚩") {
      button.text = ""
      flagCount += 1
    } else if (flagCount > 0) {
      button.text = "🚩"
      flagCount -= 1
    }

    updateFlagCount()
    checkWinCondition(controller, board)
  }

  def checkWinCondition(controller: GameController, board: Board): Unit = {
    if (flagCount == 0 && allMinesFlaggedCorrectly(board)) {
      val timeTaken = stopTimer()
      val lvl = board.length match {
        case 9 => 1
        case 13 => 2
        case _ => 3
      }
      val timeline = new Timeline {
        cycleCount = 1
        keyFrames = Seq(
          KeyFrame(Duration(2000), onFinished = _ => {
            controller.getStage.scene = new Scene {
              root = new YouWonMinesweeperView(controller, timeTaken, lvl)
            }
          })
        )
      }
      timeline.play()
    }
  }

  def allMinesFlaggedCorrectly(board: Board): Boolean = {
    for {
      row <- board.indices
      col <- board.head.indices
      if board(row)(col) == Mine && buttons(row)(col).text.value != "🚩"
    } return false
    true
  }
}