package de.axelknauf.birdswarm.ui

import akka.actor._
import akka.event.Logging

class Bird(val x: Int, val y: Int) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case _ => log.info("reveived message")
  }
}

object Bird {

  def props(x: Int, y: Int): Props = Props(classOf[Bird], x, y)
  
}
