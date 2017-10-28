package de.axelknauf.birdswarm.helloworld

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App {
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor], name = "greeter")
  helloActor ! "hello"
  helloActor ! "buenos dias"
  system.terminate
}
