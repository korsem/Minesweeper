import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets

class GameOverMinesweeperView(controller: GameController, rows: Int, cols: Int, mines: Int) extends VBox {
  spacing = 20
  padding = Insets(50)
  children = Seq(
    new Label("PRZEGRAŁEŚ") {
      style = "-fx-font-size: 32px; -fx-text-fill: red; -fx-font-weight: bold;"
    },
    new Button("Spróbuj jeszcze raz") {
      onAction = _ => controller.startMinesweeper(rows, cols, mines)
    },
    new Button("Wróć do menu") {
      onAction = _ => controller.returnToMenu()
    }
  )
}
