import GameBoard.stopTimer
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.util.Duration

class SudokuView(lvl: Int, controller: GameController) extends VBox {
  padding = Insets(20)
  spacing = 10

  private var board = SudokuBoard.generateBoard(lvl, controller)
  private val timerLabel = new Label("Time: 0s") {
    style = "-fx-font-weight: bold"
  }

  private var secondsElapsed = 0
  private val timeline = new Timeline {
    cycleCount = Timeline.Indefinite
    keyFrames = Seq(
      KeyFrame(Duration(1000), onFinished = _ => {
        secondsElapsed += 1
        timerLabel.text = s"Time: $secondsElapsed s"
      })
    )
  }
  timeline.play()

  private val gridPane = SudokuBoard.renderBoard(board, controller)

  children = Seq(
    new Label("left click to fill cell, right click to add note number, middle click to clear cell"),
    timerLabel,
    gridPane,
    new Button("Powrót do menu") {
      onAction = _ => controller.returnToMenu()
    },
    new Button("Sprawdź rozwiązanie") {
      onAction = _ => {
        val incorrectCells = SudokuBoard.checkUserBoard(board)
        if (incorrectCells.isEmpty) {
          print("You won!")
          showSolution()
          val timeTaken = stopTimer()
          val timeline = new Timeline {
            cycleCount = 1
            keyFrames = Seq(
              KeyFrame(Duration(2000), onFinished = _ => {
                controller.getStage.scene = new Scene {
                  root = new YouWonSudokuView(controller, timeTaken)
                }
              })
            )
          }
          timeline.play()
        } else {
          print("You lost!")
          // highlightIncorrectCells(incorrectCells) doesnt work
          val timeline = new Timeline {
            cycleCount = 1
            keyFrames = Seq(
              KeyFrame(Duration(3000), onFinished = _ => {
                controller.getStage.scene = new Scene {
                  root = new SudokuGameOverView(controller, lvl)
                }
              })
            )
          }
          timeline.play()
        }
      }
    },
    new Button("Wyczyść planszę") {
      onAction = _ => {
        board = SudokuBoard.boardToInitial(board, controller)
        print(board)
        gridPane.children.clear()
        gridPane.children.addAll(SudokuBoard.renderBoard(board, controller).children)
      }
    }
  )

  def stopTimer(): Int = {
    timeline.stop()
    secondsElapsed
  }

  SudokuBoard.setTimerStopper(stopTimer)

  private def showSolution(): Unit = {
    board = SudokuBoard.boardToValid(board, controller)
    gridPane.children.clear()
    gridPane.children.addAll(SudokuBoard.renderBoard(board, controller).children)
  }

  private def highlightIncorrectCells(incorrectCells: Seq[(Int, Int)]): Unit = {
    incorrectCells.foreach { case (row, col) =>
      val cell = gridPane.lookup(s"#cell-$row-$col").asInstanceOf[SudokuBoard.Cell]
      if (cell != null) {
        cell.changeBackgroundColor("red")
      } else {
        print(s"Cell $row-$col not found")
      }
    }
  }
  // xception in thread "JavaFX Application Thread" java.lang.ClassCastException: class scalafx.scene.LowerPriorityIncludes$$anon$4 cannot be cast to class SudokuBoard$Cell (scalafx.scene.LowerPriorityIncludes$$anon$4 and SudokuBoard$Cell are in unnamed module of loader 'app')
  //	at SudokuView.highlightIncorrectCells$$anonfun$1(SudokuView.scala:98)
  //	at scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
  //	at scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
  //	at scala.collection.immutable.Vector.foreach(Vector.scala:2124)
  //	at SudokuView.SudokuView$$highlightIncorrectCells(SudokuView.scala:97)
  //	at SudokuView$$anon$4.$init$$$anonfun$2(SudokuView.scala:59)
  //	at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
  //	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:232)
  //	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:189)
  //	at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
  //	at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:49)
  //	at javafx.event.Event.fireEvent(Event.java:198)
  //	at javafx.scene.Node.fireEvent(Node.java:8917)
  //	at javafx.scene.control.Button.fire(Button.java:203)
  //	at com.sun.javafx.scene.control.behavior.ButtonBehavior.mouseReleased(ButtonBehavior.java:207)
  //	at com.sun.javafx.scene.control.inputmap.InputMap.handle(InputMap.java:274)
  //	at com.sun.javafx.event.CompositeEventHandler$NormalEventHandlerRecord.handleBubblingEvent(CompositeEventHandler.java:247)
  //	at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:80)
  //	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:232)
  //	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:189)
  //	at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
  //	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
  //	at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
  //	at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:54)
  //	at javafx.event.Event.fireEvent(Event.java:198)
  //	at javafx.scene.Scene$MouseHandler.process(Scene.java:3985)
  //	at javafx.scene.Scene.processMouseEvent(Scene.java:1891)
  //	at javafx.scene.Scene$ScenePeerListener.mouseEvent(Scene.java:2709)
  //	at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:411)
  //	at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:301)
  //	at java.base/java.security.AccessController.doPrivileged(AccessController.java:399)
  //	at com.sun.javafx.tk.quantum.GlassViewEventHandler.lambda$handleMouseEvent$2(GlassViewEventHandler.java:450)
  //	at com.sun.javafx.tk.quantum.QuantumToolkit.runWithoutRenderLock(QuantumToolkit.java:430)
  //	at com.sun.javafx.tk.quantum.GlassViewEventHandler.handleMouseEvent(GlassViewEventHandler.java:449)
  //	at com.sun.glass.ui.View.handleMouseEvent(View.java:551)
  //	at com.sun.glass.ui.View.notifyMouse(View.java:937)
  //	at com.sun.glass.ui.win.WinApplication._runLoop(Native Method)
  //	at com.sun.glass.ui.win.WinApplication.lambda$runLoop$3(WinApplication.java:184)
  //	at java.base/java.lang.Thread.run(Thread.java:1589)
}