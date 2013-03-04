package misty.practices.akka.routing

import akka.actor.{Actor, Props, ActorSystem}
import akka.routing.{RoundRobinRouter, DefaultResizer}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午5:49
 */
object DynamicallyResizableRouters extends App {
	val system = ActorSystem("DynamicallyResizableRouters")
	val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)
	val router = system.actorOf(Props[ExampleActor].withRouter(RoundRobinRouter(resizer = Some(resizer))))

	class ExampleActor extends Actor {
		def receive = {
			case msg => println(msg)
		}
	}

}
