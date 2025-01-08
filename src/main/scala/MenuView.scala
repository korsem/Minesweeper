import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{VBox, HBox}
import scalafx.geometry.Insets


// TO DO: ogarnąć to okno
class MenuView(controller: GameController) extends VBox {
  spacing = 10
  padding = Insets(20)
  children = Seq(
    new Label("Wybierz grę:"),
    new HBox {
      spacing = 10
      children = Seq(
        new Button("Saper") {
          onAction = _ => controller.startMinesweeper(9, 9, 10) // domyślnie beginner
        },
        new Button("Sudoku") {
          onAction = _ => controller.startSudoku()
        }
      )
    },
    new Label("Wybierz poziom trudności dla Sapera:"),
    new HBox {
      spacing = 10
      children = Seq(
        new Button("Łatwy (9x9, 10 min)") {
          onAction = _ => controller.startMinesweeper(9, 9, 10)
        },
        new Button("Średni (14x14, 28 min)") {
          onAction = _ => controller.startMinesweeper(14, 14, 28)
        },
        new Button("Trudny (18x18, 45 min)") {
          onAction = _ => controller.startMinesweeper(18, 18, 45)
        }
      )
    }
  )
}
