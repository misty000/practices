name := "practices"

organization := "org.misty"

version := "0.0.1"

scalaVersion := "2.10.0"

resolvers += "Local repository" at "http://127.0.0.1:8081/nexus/content/repositories/central/"

sbtVersion := "0.12.2"

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion {
	v => compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.0")
}

scalacOptions += "-P:continuations:enable"

libraryDependencies ++= Seq(
	"org.scalafx"        %% "scalafx"           % "1.0.0-M2",
	"org.scalafx"        %% "scalafx-demos"     % "1.0.0-M2",
	"com.h2database"     %  "h2"                % "1.3.170",
	"com.typesafe.slick" %% "slick"             % "1.0.0",
	"com.typesafe.slick" %% "slick-testkit"     % "1.0.0"      % "test",
	"org.slf4j"          %  "slf4j-api"         % "1.7.2",
	"ch.qos.logback"     %  "logback-core"      % "1.0.9",
	"ch.qos.logback"     %  "logback-classic"   % "1.0.9",
	"org.javassist"      %  "javassist"         % "3.17.1-GA",
	"org.apache.commons" %  "commons-lang3"     % "3.1",
	"com.alibaba"        %  "druid"             % "0.2.13",
	"org.scalatest"      %  "scalatest_2.10"    % "1.9.1"      % "test"
	"com.orientechnologies" % "orient-commons"  % "1.3.0",
	"com.orientechnologies" % "orientdb-core"   % "1.3.0",
	"com.orientechnologies" % "orientdb-object" % "1.3.0",
	"com.typesafe.akka"  % "akka-actor_2.10"    % "2.1.1",
	"com.typesafe.akka"  % "akka-remote_2.10"   % "2.1.1",
	"com.typesafe.akka"  % "akka-dataflow_2.10" % "2.1.1",
	"com.typesafe.akka"  % "akka-agent_2.10"    % "2.1.1",
	"com.typesafe.akka"  % "akka-transactor_2.10" % "2.1.1",
	"com.typesafe.akka"  % "akka-slf4j_2.10"    % "2.1.1",
	"com.typesafe.akka"  % "akka-testkit_2.10"  % "2.1.1"      % "test"
)