import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextField, Button}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets

class YouWonMineSweeperView(controller: GameController) extends VBox {
  spacing = 20
  padding = Insets(50)

  val points = 100 // Placeholder for points calculation

  children = Seq(
    new Label("WYGRAŁEŚ!") {
      style = "-fx-font-size: 32px; -fx-text-fill: green; -fx-font-weight: bold;"
    },
    new Label(s"Twoje punkty to: $points"),
    new Label("Wpisz swoje imię do rankingu:"),
    new TextField {
      promptText = "Twoje imię"
    },
    new Button("Dodaj do rankingu") {
      onAction = _ => controller.returnToMenu() // Placeholder for actual ranking logic
    }
  )
}