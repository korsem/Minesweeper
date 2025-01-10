import scalafx.animation.{KeyFrame, Timeline}
import scalafx.geometry.Insets
import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.util.Duration

class SudokuView(controller: GameController) extends VBox {
  padding = Insets(20)
  spacing = 10

  private val board = SudokuBoard.generateBoard()
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
    new Label(s"left click to fill cell"),
    new Label(s"right click to add note number"),
    new Label(s"middle click to clear cell"),
    timerLabel,
    SudokuBoard.renderBoard(board, controller),
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    }
  )

  def stopTimer(): Int = {
    timeline.stop()
    secondsElapsed
  }
  
  SudokuBoard.setTimerStopper(stopTimer)
}