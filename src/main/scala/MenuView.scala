import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

class MenuView(controller: GameController) extends VBox {
  spacing = 20
  padding = Insets(20)
  alignment = Pos.Center
  style = "-fx-background-color: linear-gradient(to bottom, #ffffff, #e0e0e0);"

  private val titleLabel = new Label("Wybierz grę i poziom trudności") {
    font = Font("Arial", 24)
    style = "-fx-font-weight: bold; -fx-text-fill: darkblue;"
    effect = new DropShadow {
      color = Color.DarkGray
      radius = 5
      spread = 0.2
    }
  }

  // images
  private val sudokuImage = new ImageView(new Image("sudoku.png")) {
    fitWidth = 100
    fitHeight = 100
    preserveRatio = true
  }

  private val minesweeperImage = new ImageView(new Image("saper.png")) {
    fitWidth = 100
    fitHeight = 100
    preserveRatio = true
  }

  // Sudoku difficulty section
  private val sudokuSection = new VBox {
    spacing = 10
    alignment = Pos.TopCenter
    children = Seq(
      sudokuImage,
      new Label("Wybierz poziom trudności dla Sudoku:") {
        font = Font("Arial", 16)
      },
      new HBox {
        spacing = 10
        alignment = Pos.TopCenter
        children = Seq(
          new Button("Łatwy") {
            onAction = _ => controller.startSudoku(1)
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          },
          new Button("Średni") {
            onAction = _ => controller.startSudoku(2)
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          },
          new Button("Trudny") {
            onAction = _ => controller.startSudoku(3)
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          }
        )
      }
    )
  }

  // saper difficulty section
  private val minesweeperSection = new VBox {
    spacing = 10
    alignment = Pos.TopCenter
    children = Seq(
      minesweeperImage,
      new Label("Wybierz poziom trudności dla Sapera:") {
        font = Font("Arial", 16)
      },
      new HBox {
        spacing = 10
        alignment = Pos.TopCenter
        children = Seq(
          new Button("Łatwy (9x9, 10 min)") {
            onAction = _ => controller.startMinesweeper(9, 9, 10) // 10 / (9*9) = 0.1234
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          },
          new Button("Średni (13x13, 26 min)") {
            onAction = _ => controller.startMinesweeper(13, 13, 26) // 26 / (13*13) = 0.153
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          },
          new Button("Trudny (17x17, 47 min)") {
            onAction = _ => controller.startMinesweeper(17, 17, 47) // 47 / (17*17) = 0.162
            style = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
          }
        )
      }
    )
  }
  
  children = Seq(
    titleLabel,
    sudokuSection,
    minesweeperSection
  )
}
