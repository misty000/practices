package misty.practices.akka.routing

import akka.actor._
import akka.routing.{RoundRobinRouter, FromConfig}
import akka.remote.routing.RemoteRouterConfig

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午3:04
 */
object RoutersInAction extends App {
	val system = ActorSystem("RoutersInAction")

	{
		val router = system.actorOf(Props[ExampleActor].withRouter(FromConfig()), "myrouter1")
	}

	{
		val router1 = system.actorOf(Props[ExampleActor].withRouter(RoundRobinRouter(nrOfInstances = 5)))
	}

	{
		val actor1 = system.actorOf(Props[ExampleActor])
		val actor2 = system.actorOf(Props[ExampleActor])
		val actor3 = system.actorOf(Props[ExampleActor])
		val routees = Vector[ActorRef](actor1, actor2, actor3)
		val router2 = system.actorOf(Props[ExampleActor].withRouter(RoundRobinRouter(routees = routees)))
	}

	{
		val addresses = Seq(
			Address("akka", "remotesys", "otherhost", 1234),
			AddressFromURIString("akka://othersys@anotherhost:1234")
		)
		val routerRemote = system.actorOf(Props[ExampleActor]
			.withRouter(RemoteRouterConfig(RoundRobinRouter(5), addresses)))
	}

	// routees优先级高于nrOfInstances
	// 配置文件中定义的router比代码优先级高


	class ExampleActor extends Actor {
		def receive = {
			case _ =>
		}
	}

}
