package de.axelknauf.birdswarm.actors

import de.axelknauf.birdswarm.actors.Message._
import akka.actor.{ Actor, Props }
import akka.event.Logging

class Bird(var x: Int, var y: Int, val vector: (Int, Int)) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Tick => {
      calcNewPosition()
      sender() ! Position(x, y)
    }
  }

  def calcNewPosition() {
    x += vector._1
    y += vector._2
  }
}

object Bird {
  def props(x: Int, y: Int, vector: (Int, Int)): Props = Props(classOf[Bird], x, y, vector)
}