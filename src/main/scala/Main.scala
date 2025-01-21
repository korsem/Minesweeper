import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

object Main extends JFXApp3 {
  override def start(): Unit = {

    Database.initialize()

    stage = new PrimaryStage {
      title = "Saper i Sudoku"
      minWidth = 9 * 50 + 40
      minHeight = 9 * 50 + 80
      resizable = false
    }

    val controller = new GameController(stage)
    controller.showMainMenu()
  }
}