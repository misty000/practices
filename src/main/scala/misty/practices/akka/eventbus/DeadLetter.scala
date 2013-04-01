package misty.practices.akka.eventbus

import akka.actor._
import akka.actor.DeadLetter
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午2:23
 */
object DeadLetterApp extends App {
	val system = ActorSystem()
	val listener = system.actorOf(Props(new Actor with ActorLogging {
		def receive = {
			case d: DeadLetter => log.debug("DeadLetter Listener: {}", d)
			case _ => log.debug("unknown")
		}
	}))
	system.eventStream.subscribe(listener, classOf[DeadLetter])

	val actor = system.actorOf(Props(new Actor with ActorLogging {
		def receive = {
			case msg => log.debug("Actor Listener: {}", msg)
		}
	}))
	actor ! "aaa"
	system.stop(actor)
	actor ! "bbb"
	actor ! "ccc"
	actor ! "ddd"
	actor ! "eee"
	actor ! "fff"
	system.shutdown()
	system.awaitTermination()
}
