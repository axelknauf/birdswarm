package de.axelknauf.birdswarm.ui

import de.axelknauf.birdswarm.actors.Message._
import de.axelknauf.birdswarm.actors._

import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }
import java.awt.Color
import javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE
import scala.collection.mutable
import scala.swing._
import scala.swing.event._
import scala.swing.BorderPanel.Position._

object BirdSwarmUI extends SimpleSwingApplication {

  val canvas = new Canvas { preferredSize = new Dimension(800, 600) }

  val system = ActorSystem("birdswarm")
  val mainLoop = system.actorOf(MainLoop.props(), "mainLoop")

  def top = new MainFrame {
    title = "BirdSwarm UI"
    peer.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE)

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
      case ButtonClicked(component) if component == startStopButton => {
        mainLoop ! { if (startStopButton.selected) Start else Stop }
        statusBar.text = "Simulation is started: " + startStopButton.selected
      }
      case MouseClicked(_, point, _, _, _) =>
        // TODO add new bird with position and trajectory (using MouseDrag event?)
        mainLoop ! NewBird(point.x, point.y)
        // TODO broadcase positions to canvas somehow
        statusBar.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.")
    }
  }

  override def shutdown() {
    // TODO fix shutting down, does not work currently
    system.terminate()
    System.exit(0)
  }

}
