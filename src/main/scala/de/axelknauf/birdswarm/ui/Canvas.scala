package de.axelknauf.birdswarm.ui

import scala.swing.Panel
import java.awt.{ Graphics2D, Color }

class Canvas extends Panel {
  var centerColor = Color.yellow
  
  var birds = List[Bird]()

  override def paintComponent(g: Graphics2D) {
    g.clearRect(0, 0, size.width, size.height)
    
    // Draw things that change on top of background
   for (bird <- birds) {
     g.setColor(bird.color)
     g.fillOval(bird.x, bird.y, bird.size, bird.size)
   }
  }

  /** Add a "bird" to list of things to display */
  def addBird(bird: Bird) {
    birds = birds :+ bird
    repaint()
  }
}

