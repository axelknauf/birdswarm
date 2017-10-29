package de.axelknauf.birdswarm.ui

import scala.swing._
import scala.swing.event._

object BirdSwarmUI extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "BirdSwarm"
    //contents = new BorderPanel {
      contents = new Button {
        text = "Click me!"
      }
    //}
  }
}
