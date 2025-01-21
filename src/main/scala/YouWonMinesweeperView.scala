import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextField, Button}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets

class YouWonMinesweeperView(controller: GameController, timeTaken: Int, lvl: Int) extends VBox {
  spacing = 20
  padding = Insets(50)
  
  val text_lvl = lvl match {
    case 1 => "Łatwy"
    case 2 => "Średni"
    case 3 => "Trudny"
  }

  val nameField = new TextField {
    promptText = "Twoje imię"
  }
  
  children = Seq(
    new Label("WYGRAŁEŚ!") {
      style = "-fx-font-size: 32px; -fx-text-fill: green; -fx-font-weight: bold;"
    },
    new Label(s"Czas gry: $timeTaken s") {
      style = "-fx-font-weight: bold"
    },
    new Label("Wpisz swoje imię do rankingu:"),
    nameField,
    new Button("Dodaj do rankingu") {
      onAction = _ => {
        Database.saveScore("minesweeper", text_lvl, nameField.text.value, timeTaken)
        controller.showRanking("minesweeper", text_lvl)
      }
    },
    new Button("Powrót do menu") {
  onAction = _ => controller.returnToMenu()
  }
  )
}
