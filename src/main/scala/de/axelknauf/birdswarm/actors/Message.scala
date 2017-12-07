package de.axelknauf.birdswarm.actors

sealed trait Message

object Message {
    // System shutdown
    case class Shutdown() extends Message
    // Change active/inactive state (if ticks are sent)
    case class Start() extends Message
    case class Stop() extends Message
    // Used by main loop and interaction
    case class Tick() extends Message
    case class NewBird(x: Int, y: Int) extends Message
    case class Position(x: Int, y: Int) extends Message
}
