package de.axelknauf.birdswarm.ui

import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }
import akka.event.Logging
import language.postfixOps
import scala.collection.mutable

sealed trait Message
// System startup and shutdown
case class Bootstrap() extends Message
case class Shutdown() extends Message
// Change active/inactive state (if ticks are sent)
case class Start() extends Message
case class Stop() extends Message
// Used by main loop and interaction
case class Tick(millis: Long) extends Message
case class NewBird(x: Int, y: Int) extends Message
case class Position(x: Int, y: Int) extends Message

object Main extends App {


  val system = ActorSystem("birdswarm")
  val mainLoop = system.actorOf(MainLoop.props())

  mainLoop ! Bootstrap
  mainLoop ! Start
  mainLoop ! Stop
  mainLoop ! Shutdown

  system.terminate()

}

class MainLoop extends Actor {

  import context._

  val log = Logging(context.system, this)
  var birds: mutable.Set[ActorRef] = mutable.Set()

  // TODO how to extract common message behavior?
  def commonBehavior: Receive = {
    case Shutdown => context.stop(self)
    case _ => log.info("Unknown message!")
  }

  def activeMainLoop: Receive = {
    case NewBird(x, y) => birds.add(system.actorOf(Bird2.props(x, y)))
    case Position(x, y) => log.info(s"Received position: ${x}, ${y}.")
    case Stop => {
      log.info("Mainloop stopping")
      become(inactiveMainLoop)
    }
    case message => commonBehavior(message)
  }

  def inactiveMainLoop: Receive = {
    case Start => {
      // TODO add tick scheduling
      log.info("Mainloop starting")
      become(activeMainLoop)
    }
    case message => commonBehavior(message)
  }

  def receive = {
    case Bootstrap => {
      log.info("Mainloop bootstrapping")
      become(activeMainLoop)
    }
    case Shutdown => {
      log.info("Mainloop shutting down")
      context.stop(self)
    }
  }
}

object MainLoop {
  def props(): Props = Props(classOf[MainLoop])
}

// TODO add ref to mainloop to bird in order to send position
class Bird2(var x: Int, var y: Int) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Tick(millis) => {
      log.info(s"reveived tick: ${millis}")
      // sender() | Position(1, 2)
    }
  }
}

object Bird2 {
  def props(x: Int, y: Int): Props = Props(classOf[Bird2], x, y)
}
