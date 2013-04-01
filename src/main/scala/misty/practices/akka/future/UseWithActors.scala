package misty.practices.akka.future

import concurrent.Await
import concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.{Actor, Props, ActorSystem}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午3:48
 */
object UseWithActors extends App {
	val system = ActorSystem()
	val actor = system.actorOf(Props(new Actor {
		def receive = {
			case m =>
				println(m)
				sender ! "123"
		}
	}))

	// Timeout.durationToTimeout(DurationInt#seconds)
	implicit val timeout: Timeout = 5.seconds

	//	implicit val t = Timeout(6.seconds) // case Timeout提供的apply方法


	val future = actor ? "msg"


	val result = Await.result(future, timeout.duration)
	println(result)
	system.shutdown()
}
