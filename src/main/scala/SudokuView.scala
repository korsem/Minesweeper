import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}

class SudokuView(controller: GameController) extends VBox {
  children = Seq(
    new Label("Sudoku - funkcjonalność w trakcie tworzenia."),
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    }
  )
}
