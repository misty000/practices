package misty.practices.akka

import akka.actor.{Props, ActorSystem, Actor}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午1:57
 */
object SpecificActor extends App {
	val system = ActorSystem("SpecificSystem")
	val actor = system.actorOf(Props[SpecificActor], "specificActor")
	actor ! MyMsg("a")
	actor ! "a"
	actor ! MyMsg("b")
	actor ! "b"
	actor ! MyMsg("c")
	system.shutdown()
}

abstract class GenericActor extends Actor {
	def specificMessageHandler: Receive

	def genericMessageHandler: Receive = {
		case event => printf("generic: %s\n", event)
	}

	def receive = specificMessageHandler orElse genericMessageHandler
}

class SpecificActor extends GenericActor {
	override def specificMessageHandler = {
		case event: MyMsg => printf("specific: %s\n", event.subject)
	}
}

case class MyMsg(subject: String)