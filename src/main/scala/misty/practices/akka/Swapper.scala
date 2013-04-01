package misty.practices.akka

import akka.actor.{Kill, Props, ActorSystem, Actor}
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午12:37
 */
object Swapper extends App {

	case object Swap

	val system = ActorSystem("SwapperSystem")
	val swap = system.actorOf(Props[Swapper], "swapper")

	swap ! Swap
	swap ! Swap
	swap ! Swap
	swap ! Swap
	swap ! Swap

	swap ! Kill

	swap ! Swap
	swap ! Swap

	system.shutdown()
}

class Swapper extends Actor {

	import Swapper.Swap

	val logger = Logging(context.system, this)

	def swap: Receive = {
		case Swap =>
			logger.info("Ho")
			context.unbecome()
	}

	override def receive = {
		case Swap =>
			logger.info("Hi")
			context become swap
	}
}
