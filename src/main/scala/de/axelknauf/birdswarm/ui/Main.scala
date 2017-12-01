package de.axelknauf.birdswarm.ui

import scala.concurrent.duration._
import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill, Timers }
import akka.event.Logging
import language.postfixOps
import scala.collection.mutable

sealed trait Message
// System shutdown
case class Shutdown() extends Message
// Change active/inactive state (if ticks are sent)
case class Start() extends Message
case class Stop() extends Message
// Used by main loop and interaction
case class Tick() extends Message
case class NewBird(x: Int, y: Int) extends Message
case class Position(x: Int, y: Int) extends Message

object Main extends App {


  val system = ActorSystem("birdswarm")
  val mainLoop = system.actorOf(MainLoop.props())

  Thread.sleep(500)
  mainLoop ! Start
  Thread.sleep(500)
  mainLoop ! NewBird(1, 2)
  Thread.sleep(500)
  mainLoop ! NewBird(4, 2)
  Thread.sleep(500)
  mainLoop ! NewBird(3, 5)
  Thread.sleep(500)
  mainLoop ! Stop
  Thread.sleep(500)
  mainLoop ! Shutdown

  system.terminate()

}

class MainLoop extends Actor with Timers {

  import context._

  val log = Logging(context.system, this)
  var birds: mutable.Set[ActorRef] = mutable.Set()

  def receive = {
    // LIFECYCLE ----------------------------------------
    case Shutdown => {
      log.info("Shutting down")
      context.stop(self)
    }
    case Start => {
      log.info("Mainloop starting")
      timers.startPeriodicTimer(Tick, Tick, 500.millis)
    }
    case Stop => {
      log.info("Mainloop stopping")
      timers.cancel(Tick)
    }
    // PROCESSING ----------------------------------------
    case NewBird(x, y) => {
      log.info(s"Creating new bird at ${x}, ${y}")
      birds.add(system.actorOf(Bird.props(x, y)))
    }
    case Position(x, y) => log.info(s"Bird ${sender()} is at position: ${x}, ${y}.")
    case Tick => {
      log.info("Tick!")
      birds.par.foreach(_ ! Tick)
    }
    case msg => log.info(s"Unknown message ${msg}!")
  }
}

object MainLoop {
  def props(): Props = Props(classOf[MainLoop])
}

class Bird(var x: Int, var y: Int) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Tick => {
      calcNewPosition()
      sender() ! Position(x, y)
    }
  }

  def calcNewPosition() {
    log.info("Calculating new position (NOOP).")
  }
}

object Bird {
  def props(x: Int, y: Int): Props = Props(classOf[Bird], x, y)
}
