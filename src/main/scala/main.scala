import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}

object Main extends JFXApp3 {

  override def start(): Unit = {
    stage = new PrimaryStage {
      title = "Minesweeper - ScalaFX"
      scene = new Scene {
        root = new VBox {
          spacing = 10
          padding = Insets(20)
          children = Seq(
            new Label("Wybierz poziom trudności:"),
            new HBox {
              spacing = 10
              children = Seq(
                new Button("Beginner (9x9, 10 min)") {
                  onAction = _ => startGame(9, 9, 10)
                },
                new Button("Intermediate (16x16, 40 min)") {
                  onAction = _ => startGame(16, 16, 40)
                },
                new Button("Advanced (24x24, 99 min)") {
                  onAction = _ => startGame(24, 24, 99)
                }
              )
            }
          )
        }
      }
    }
  }

  // Metoda startująca grę dla wybranego poziomu
  def startGame(rows: Int, cols: Int, mines: Int): Unit = {
    stage.scene = new Scene {
      root = new VBox {
        spacing = 10
        padding = Insets(20)
        children = Seq(
          new Label(s"Rozpoczęto grę na planszy $rows x $cols z $mines minami."),
          new Button("Powrót do menu") {
            onAction = _ => start() // Powrót do menu głównego
          }
        )
      }
    }
  }
}
