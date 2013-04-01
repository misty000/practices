package misty.practices.akka.dispatchers

import akka.actor.{Actor, Props, PoisonPill, ActorSystem}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config
import akka.event.{Logging, LoggingAdapter}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午2:01
 */
object MyPrioMailbox extends App {
	val system = ActorSystem("MyPrioMailboxSystem")
	val a = system.actorOf(
		Props(new Actor {
			val log: LoggingAdapter = Logging(context.system, this)

			self ! 'lowpriority
			self ! 'lowpriority
			self ! 'highpriority
			self ! 'pigdog
			self ! 'pigdog2
			self ! 'pigdog3
			self ! 'highpriority
			self ! PoisonPill

			def receive = {
				case x ⇒ log.info(x.toString)
			}
		}).withDispatcher("prio-dispatcher"))

	system.shutdown()

	/*
	Logs:
	'highpriority
	'highpriority
	'pigdog
	'pigdog2
	'pigdog3
	'lowpriority
	'lowpriority
	*/
}

class MyPrioMailbox(settings: ActorSystem.Settings, config: Config)
	extends UnboundedPriorityMailbox(
		// Create a new PriorityGenerator, lower prio means more important
		PriorityGenerator {
			// 'highpriority messages should be treated first if possible
			case 'highpriority ⇒ 0

			// 'lowpriority messages should be treated last if possible
			case 'lowpriority ⇒ 2

			// PoisonPill when no other left
			case PoisonPill ⇒ 3

			// We default to 1, which is in between high and low
			case otherwise ⇒ 1
		})