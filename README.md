# Flock of Birds simulation

Simple simulation of a flock of birds using Akka actors and a Scala Swing UI.

## TODO

* Add readme to repo
* Fix shutting down of actor system
* Broadcast positions of birds and collect into Seq/Map (?)
* Implement displaying birds on UI with collected positions
* Fix UI scaling on high DPI displays
* Derive new bird's trajectory from mouse-drag event
* Add behavior to birds to flock together, but not too close
* Add behavior to birds to avoid walls and change course
* Add obstacles which disperse bird swarms ("cats")
* Add behavior to birds to avoid mouse pointer (?)
* Make simulation parameters (tick interval etc.) configurable, e. g. via UI
* Enhance simulation graphics (colors, sprites)
