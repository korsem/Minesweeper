import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

object Main extends JFXApp3 {
  override def start(): Unit = {
    stage = new PrimaryStage {
      title = "Game Menu"
      minWidth = 9 * 50 + 40
      minHeight = 9 * 50 + 80
      resizable = false
    }

    val controller = new GameController(stage)
    controller.showMainMenu()
  }
}