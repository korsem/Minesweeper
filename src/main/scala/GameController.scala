import scalafx.stage.Stage
import scalafx.scene.Scene

class GameController(primaryStage: Stage) {
  private val stage = primaryStage
  // GameController jest odpowiedzialny za zarządzanie widokami

  def showMainMenu(): Unit = {
    stage.scene = new Scene {
      root = new MenuView(GameController.this)
    }
  }

  def startMinesweeper(rows: Int, cols: Int, mines: Int): Unit = {
    stage.scene = new Scene {
      root = new MinesweeperView(rows, cols, mines, GameController.this)
    }
  }

  def startSudoku(lvl: Int): Unit = {
    stage.scene = new Scene {
      root = new SudokuView(lvl, GameController.this)
    }
  }

  def showRanking(game: String, level: String): Unit = {
    stage.scene = new Scene {
      root = new RankingView(GameController.this, game, level)
    }
  }

  def returnToMenu(): Unit = showMainMenu()

  def getStage: Stage = stage
}