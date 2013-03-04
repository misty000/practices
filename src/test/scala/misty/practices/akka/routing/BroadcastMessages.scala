package misty.practices.akka.routing

import akka.actor.{Actor, Props, ActorSystem}
import akka.routing.{Broadcast, RandomRouter}
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午5:41
 */
object BroadcastMessages extends App {
	val system = ActorSystem("BroadcastMessages")
	val router = system.actorOf(Props[MyActor].withRouter(RandomRouter(5)), "router")

	router ! Broadcast("Watch out for Davy Jones' locker")

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	class MyActor extends Actor {
		def receive = {
			case msg => println(msg)
		}
	}

}
