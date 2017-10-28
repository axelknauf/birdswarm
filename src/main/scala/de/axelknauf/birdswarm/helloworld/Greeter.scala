package de.axelknauf.birdswarm.helloworld

import akka.actor.Actor

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _       => println("huh?")
  }
}
