package de.axelknauf.birdswarm.ui

import scala.collection.mutable
import scala.swing.Panel
import java.awt.{ Graphics2D, Color }

class Canvas extends Panel {

  val birdSize = 20

  var swarm = mutable.Map[String, (Int, Int)]()

  override def paintComponent(g: Graphics2D) {
    g.clearRect(0, 0, size.width, size.height)
    for (pos <- swarm.values) {
      g.fillOval(pos._1, pos._2, birdSize, birdSize)
    }
  }

  def setPositions(newPositions: mutable.Map[String, (Int, Int)]) {
    swarm = newPositions
    repaint()
  }
}

