import scalafx.scene.layout.{VBox, GridPane}
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Insets
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration
import scalafx.Includes.*

class MinesweeperView(rows: Int, cols: Int, mines: Int, controller: GameController) extends VBox {
  spacing = 10
  padding = Insets(20)

  private val board = GameBoard.generateBoard(rows, cols, mines)
  private val flagCountLabel = new Label(s"Flags left: ${GameBoard.flagCount}")
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
  

  children = Seq(
    new Label(s"Saper: Rozmiar planszy $rows x $cols, Liczba min: $mines"),
    timerLabel,
    flagCountLabel,
    GameBoard.renderBoard(board, controller),
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    }
  )

  def updateFlagCount(): Unit = {
    flagCountLabel.text = s"Flags left: ${GameBoard.flagCount}"
  }
  
  def stopTimer(): Int = {
    timeline.stop()
    secondsElapsed
  }

  GameBoard.setFlagCountUpdater(updateFlagCount)
  GameBoard.setTimerStopper(stopTimer)
}