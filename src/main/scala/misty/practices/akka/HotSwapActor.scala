package misty.practices.akka

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午12:10
 */
object HotSwapActor extends App {
	val system = ActorSystem("system")
	val actor = system.actorOf(Props[MyActor], "hotSwapActor")

	actor ! "foo"
	actor ! "foo"
	actor ! "bar"
	actor ! "bar"
	actor ! "foo"
	actor ! "foo"
	actor ! "bar"
	actor ! "bar"

	system.shutdown()
	system.awaitTermination()

	class MyActor extends Actor {
		val actor = context.actorOf(Props[HotSwapActor], "hotSwapActor")

		def receive = {
			case m@"foo" => actor ! m
			case m@"bar" => actor ! m
			case m => println(m)
		}
	}

}

class HotSwapActor extends Actor {
	private val logger = Logging(context.system, this)

	def angry: Receive = {
		case m@"foo" => logger.info(m); sender ! "I am already angry"
		case m@"bar" => logger.info(m); context.become(happy)
	}

	def happy: Receive = {
		case m@"bar" => logger.info(m); sender ! "I am already happy :-)"
		case m@"foo" => logger.info(m); context.become(angry)
	}

	override def receive = {
		case m@"foo" => logger.info(m); context.become(angry)
		case m@"bar" => logger.info(m); context.become(happy)
	}
}
