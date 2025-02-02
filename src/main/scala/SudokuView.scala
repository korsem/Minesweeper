import GameBoard.stopTimer
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.util.Duration

class SudokuView(lvl: Int, controller: GameController) extends VBox {
  padding = Insets(20)
  spacing = 10

  private var board = SudokuBoard.generateBoard(lvl, controller)
  private val timerLabel = new Label("Time: 0s") {
    style = "-fx-font-weight: bold"
  }

  private var secondsElapsed = 0
  private val timeline = new Timeline {
    cycleCount = Timeline.Indefinite
    keyFrames = Seq(
      KeyFrame(Duration(1000), onFinished = _ => {
        secondsElapsed += 1
        timerLabel.text = s"Time: $secondsElapsed s"
      })
    )
  }
  timeline.play()

  private val gridPane = SudokuBoard.renderBoard(board, controller)

  children = Seq(
    new Label("left click to fill cell, right click to add note number, middle click to clear cell"),
    timerLabel,
    gridPane,
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    },
    new Button("Sprawdź rozwiązanie") {
      onAction = _ => {
        val incorrectCells = SudokuBoard.checkUserBoard()
        if (incorrectCells.isEmpty) {
          showSolution()
          val timeTaken = stopTimer()
          val timeline = new Timeline {
            cycleCount = 1
            keyFrames = Seq(
              KeyFrame(Duration(2000), onFinished = _ => {
                controller.getStage.scene = new Scene {
                  root = new YouWonSudokuView(controller, timeTaken, lvl)
                }
              })
            )
          }
          timeline.play()
        } else {
          // highlightIncorrectCells(incorrectCells)
          val timeline = new Timeline {
            cycleCount = 1
            keyFrames = Seq(
              KeyFrame(Duration(3000), onFinished = _ => {
                controller.getStage.scene = new Scene {
                  root = new SudokuGameOverView(controller, lvl)
                }
              })
            )
          }
          timeline.play()
        }
      }
    },
    new Button("Wyczyść planszę") {
      onAction = _ => {
        board = SudokuBoard.boardToInitial(board, controller)
        print(board)
        gridPane.children.clear()
        gridPane.children.addAll(SudokuBoard.renderBoard(board, controller).children)
      }
    }
  )

  def stopTimer(): Int = {
    timeline.stop()
    secondsElapsed
  }

  SudokuBoard.setTimerStopper(stopTimer)

  private def showSolution(): Unit = {
    board = SudokuBoard.boardToValid(board, controller)
    gridPane.children.clear()
    gridPane.children.addAll(SudokuBoard.renderBoard(board, controller).children)
  }

  private def highlightIncorrectCells(incorrectCells: Seq[(Int, Int)]): Unit = {
    // todo - można w przyszłości naprawić tą metodę, obecnie nie jest używana
    incorrectCells.foreach { case (row, col) =>
      val cell = gridPane.lookup(s"#cell-$row-$col").asInstanceOf[SudokuBoard.Cell]
      if (cell != null) {
        cell.changeBackgroundColor("red")
      } else {
        print(s"Cell $row-$col not found")
      }
    }
  }
}