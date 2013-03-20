package misty.practices.akka.dispatchers

import akka.actor.{ActorRef, ActorSystem}
import akka.dispatch.{Envelope, MessageQueue, QueueBasedMessageQueue, UnboundedMessageQueueSemantics}
import com.typesafe.config.Config
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午2:21
 */
class MyUnboundedMailbox extends akka.dispatch.MailboxType {
	// This constructor signature must exist, it will be called by Akka
	def this(settings: ActorSystem.Settings, config: Config) = this()

	// The create method is called to create the MessageQueue
	final override def create(owner: Option[ActorRef], system: Option[ActorSystem]): MessageQueue =
		new QueueBasedMessageQueue with UnboundedMessageQueueSemantics {
			final val queue = new ConcurrentLinkedQueue[Envelope]()
		}
}