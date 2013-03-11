name := "practices"

organization := "org.misty"

version := "0.0.1"

scalaVersion := "2.10.0"

resolvers += "Local repository" at "http://127.0.0.1:8081/nexus/content/repositories/central/"

libraryDependencies ++= Seq(
	"com.h2database" % "h2" % "1.3.170",
	"org.apache.openjpa" % "openjpa" % "2.2.1",
	"org.slf4j" % "slf4j-api" % "1.7.2",
    "ch.qos.logback" % "logback-classic" % "1.0.9",
	"ch.qos.logback" % "logback-core" % "1.0.9",
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)