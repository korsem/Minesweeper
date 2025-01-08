import scalafx.stage.Stage
import scalafx.scene.Scene

class GameController(primaryStage: Stage) {
  private val stage = primaryStage

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

  def startSudoku(): Unit = {
    stage.scene = new Scene {
      root = new SudokuView(GameController.this)
    }
  }

  def returnToMenu(): Unit = showMainMenu()

  def getStage: Stage = stage
}