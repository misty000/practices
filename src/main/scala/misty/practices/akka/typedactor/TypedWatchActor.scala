package misty.practices.akka.typedactor

import akka.actor.TypedActor._
import akka.actor.{ActorRef, SupervisorStrategy}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午5:27
 */
trait TypedWatchActor

class TypedWatchActorImpl
	extends TypedWatchActor
	with Receiver
	with Supervisor
	with PreStart
	with PostStop
	with PreRestart
	with PostRestart {

	def supervisorStrategy(): SupervisorStrategy = ???

	def onReceive(message: Any, sender: ActorRef) {}

	def preStart() {}

	def postStop() {}

	def postRestart(reason: Throwable) {}

	def preRestart(reason: Throwable, message: Option[Any]) {}
}
