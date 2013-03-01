name := "practices"

organization := "org.misty"

version := "0.0.1"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "com.orientechnologies" % "orient-commons" % "1.3.0",
    "com.orientechnologies" % "orientdb-core" % "1.3.0",
    "com.orientechnologies" % "orientdb-object" % "1.3.0",
    "org.javassist" % "javassist" % "3.17.1-GA",
    "com.typesafe.akka" % "akka-actor_2.10" % "2.1.0",
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)