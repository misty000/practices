package misty.practices.akka

import akka.actor.Actor
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午10:56
 */
class MyActor extends Actor {
	val log = Logging(context.system, this)

	override def preStart() {
		println("preStart")
	}

	def receive = {
		case "test" => log.info("received test")
		case _ => log.info("received unknown message")
	}
}
