package misty.practices.akka

import akka.actor.{Terminated, ActorSystem, Props, Actor}
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 上午12:24
 */
object WatchActor extends App {
	val system = ActorSystem()

	val actor = system.actorOf(Props[WatchActor], "watchActor")
	actor ! "kill"

	system.shutdown()
}

class WatchActor extends Actor {
	val logger = Logging(context.system, this)
	val child = context.actorOf(Props.empty, "child")
	context.watch(child)
	var lastSender = context.system.deadLetters

	override def preStart() {

	}

	override def receive = {
		case "kill" =>
			logger.info("kill")
			logger.info(sender.toString())

			context.stop(child)
			lastSender = sender
		case Terminated(`child`) =>
			logger.info("Terminated")
			logger.info(lastSender.toString())

			lastSender ! "finished"
		case _ =>
	}
}
