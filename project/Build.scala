import sbt._
import Keys._

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-12
 * Time: 下午12:06
 */
object build extends Build {
	lazy val main = Project(
		id = "practices",
		base = file(".")
	)

	lazy val classdumpProject = Project(
		id = "classdump",
		base = file("classdump"),
		settings = Defaults.defaultSettings ++ Seq(
			scalaVersion := "2.10.0",
			resolvers += "Local repository" at "http://127.0.0.1:8081/nexus/content/repositories/central/",
			libraryDependencies ++= Seq(
				"org.clapper" %% "classutil" % "1.0.1"
			)
		)
	) dependsOn (main % "compile;test")
}
