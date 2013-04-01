package misty.practices.akka.remoting

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午1:03
 */
object RoutersWithRemoteDestinations {
	val config = ConfigFactory.parseString(
		"""
		  |akka {
		  |	actor {
		  |		deployment {
		  |			/serviceA/aggregation {
		  |				router = "round-robin"
		  |    			nr-of-instances = 10
		  |       		target {
		  |         		nodes = [
		  |           			"akka://ServerActorSystem@127.0.0.1:2554",
		  |              		"akka://ServerActorSystem@127.0.0.1:2555"
		  |             		]
		  |         	}
		  |			}
		  |		}
		  | }
		  |}
		""".stripMargin)
	val system = ActorSystem("RoutersWithRemoteDestinations", config)

	system.shutdown()
}
