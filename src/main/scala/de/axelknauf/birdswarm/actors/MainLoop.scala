package de.axelknauf.birdswarm.actors

import de.axelknauf.birdswarm.actors.Message._
import de.axelknauf.birdswarm._

import scala.concurrent.duration._
import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill, Timers }
import akka.event.Logging
import language.postfixOps
import scala.collection.mutable

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
      // TODO add vector of trajectory to new bird
      birds.add(system.actorOf(Bird.props(x, y, (4, 4)), s"Bird-${birds.size}"))
    }
    case Position(x, y) => {
      // TODO save position somewhere to make accessible from UI
      log.info(s"Bird ${sender()} is at position: ${x}, ${y}.")
    }
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
