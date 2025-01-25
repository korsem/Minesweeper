import scalafx.scene.control.{Label,  Button}
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets

class RankingView(controller: GameController, game: String, level: String) extends VBox {
  spacing = 20
  padding = Insets(50)

  val scores = Database.getScores(game, level)
  // wyświetlanie list powinno być posortowane rosnąco po czasie
  children = Seq(
    new Label(s"Ranking $game - $level") {
      style = "-fx-font-size: 24px; -fx-font-weight: bold;"
    }
  ) ++ scores.map { case (name, time) =>
    new Label(s"$name: $time s")
  } ++ Seq(
    new Button("Back to menu") {
      onAction = _ => controller.returnToMenu()
    }
  )
}