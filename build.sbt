name := """snakesandladders-web"""
organization := "de.htwg"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.1"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += ("org.scala-lang.modules" %% "scala-swing" % "3.0.0").cross(CrossVersion.for3Use2_13)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.3"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "de.htwg.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "de.htwg.binders._"
