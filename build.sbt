name := "birdswarm"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test
)
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.scala-lang" % "scala-library-all" % "2.12.4"
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10+"

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.2")

fork := true
// TODO scale UI on high dpi displays
// javaOptions in run := Seq("-Dsun.java2d.dpiaware=true")

mainClass in (Compile, run) := Some("de.axelknauf.birdswarm.ui.BirdSwarmUI")

