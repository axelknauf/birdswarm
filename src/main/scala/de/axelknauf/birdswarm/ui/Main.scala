package de.axelknauf.birdswarm.ui

import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }
import akka.event.Logging
import language.postfixOps
import scala.collection.mutable

case class Tick(millis: Long)
case class Position(x: Int, y: Int)

object Main extends App {

  var swarm = mutable.Map[String, (Int, Int)]()

  val system = ActorSystem("birdswarm")
  val mainLoop = system.actorOf(MainLoop.props())
  val bird = system.actorOf(Bird2.props())

  bird ! Tick(System.currentTimeMillis())
  mainLoop ! Position(1, 2)
  bird ! PoisonPill
  mainLoop ! PoisonPill

  system.terminate()

}

class MainLoop extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Position(x, y) => log.info(s"Received position: ${x}, ${y}.")
    case _ => log.info("Unknown message")
  }
}

object MainLoop {
  def props(): Props = Props(classOf[MainLoop])
}

class Bird2 extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Tick(millis) => {
      log.info(s"reveived tick: ${millis}")
      // sender() | Position(1, 2)
    }
    case _ => log.info("Unknown message")
  }
}

object Bird2 {
  def props(): Props = Props(classOf[Bird2])
}
