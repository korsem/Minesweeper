import scalafx.scene.layout.{VBox, GridPane}
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Insets

class MinesweeperView(rows: Int, cols: Int, mines: Int, controller: GameController) extends VBox {
  spacing = 10
  padding = Insets(20)

  private val board = GameBoard.generateBoard(rows, cols, mines)

  children = Seq(
    new Label(s"Saper: Rozmiar planszy $rows x $cols, Liczba min: $mines"),
    GameBoard.renderBoard(board, controller),
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    }
  )
}