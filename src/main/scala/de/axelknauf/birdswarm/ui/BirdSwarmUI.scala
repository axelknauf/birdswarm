package de.axelknauf.birdswarm.ui

import scala.swing._
import scala.swing.event._
import scala.swing.BorderPanel.Position._
import java.awt.Color

object BirdSwarmUI extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "BirdSwarm UI"

    val statusBar       = new Label        { text = "BirdSwarm v0.1 - no stats available" }
    val startStopButton = new ToggleButton { text = "Start/Stop" }
    val canvas          = new Canvas       { preferredSize = new Dimension(800, 600) }

    contents = new BorderPanel {
      layout(startStopButton) = North
      layout(canvas) = Center
      layout(statusBar) = South
    }
    size = new Dimension(800, 600)

    listenTo(startStopButton)
    listenTo(canvas.mouse.clicks)

    reactions += {
      case ButtonClicked(component) if component == startStopButton =>
        statusBar.text = "Start/Stop Button has been clicked: " + startStopButton.selected
      case MouseClicked(_, point, _, _, _) =>
        canvas.addBird(new Bird(point.x, point.y, Color.black, 20))
        statusBar.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.") 
    }
  }
}
