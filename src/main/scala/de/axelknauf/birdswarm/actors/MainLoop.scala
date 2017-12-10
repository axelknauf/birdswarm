package de.axelknauf.birdswarm.actors

import de.axelknauf.birdswarm.ui._
import de.axelknauf.birdswarm.actors.Message._
import de.axelknauf.birdswarm._

import scala.concurrent.duration._
import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill, Timers }
import akka.event.Logging
import language.postfixOps
import scala.collection.mutable

class MainLoop(val canvas: Canvas) extends Actor with Timers {

  import context._

  val log = Logging(context.system, this)
  val tickInterval = 500.millis

  var birds: mutable.Set[ActorRef] = mutable.Set()
  val positions = mutable.Map[String, (Int, Int)]()

  def receive = {
    // LIFECYCLE ----------------------------------------
    case Start => {
      log.info("Mainloop starting")
      timers.startPeriodicTimer(Tick, Tick, tickInterval)
    }
    case Stop => {
      log.info("Mainloop stopping")
      timers.cancel(Tick)
    }
    // PROCESSING ----------------------------------------
    case NewBird(x, y) => {
      log.info(s"Creating new bird at ${x}, ${y}")
      // TODO add vector of trajectory to new bird
      birds.add(system.actorOf(Bird.props(x, y, (4, 4)), s"Bird-${birds.size}"))
    }
    case Position(x, y) => {
      log.info(s"Bird ${sender()} is at position: ${x}, ${y}.")
      positions(sender().path.name) = (x, y)
      canvas.setPositions(positions)
    }
    case Tick => {
      log.info("Tick!")
      birds.par.foreach(_ ! Tick)
    }
    case msg => log.info(s"Unknown message ${msg}!")
  }
}

object MainLoop {
  def props(canvas: Canvas): Props = Props(classOf[MainLoop], canvas)
}
