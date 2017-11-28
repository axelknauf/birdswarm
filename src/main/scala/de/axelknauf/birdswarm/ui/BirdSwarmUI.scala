package de.axelknauf.birdswarm.ui

import scala.collection.mutable
import scala.swing._
import scala.swing.event._
import scala.swing.BorderPanel.Position._
import java.awt.Color
import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }


object BirdSwarmUI extends SimpleSwingApplication {

  val canvas = new Canvas { preferredSize = new Dimension(800, 600) }

  val system = ActorSystem("birdswarm")

  var swarm = mutable.Map[String, (Int, Int)]()

  // set up graphical UI
  def top = new MainFrame {
    title = "BirdSwarm UI"

    val statusBar       = new Label        { text = "BirdSwarm v0.1 - no stats available" }
    val startStopButton = new ToggleButton { text = "Start/Stop" }

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
        swarm("id-" + System.currentTimeMillis()) = (point.x, point.y)
        canvas.setPositions(swarm)
        statusBar.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.") 
    }
  }

  override def shutdown() {
    system.terminate()
  }
}
