package misty.practices.akka

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging
import actors.threadpool.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午2:08
 */
trait ComposableActor extends Actor {
	protected val logger = Logging(context.system, this)

	private var receives: List[Receive] = List()
	private var buf: Receive = _

	val defaultReceive: Receive = {
		case e => println("default " + e)
	}
	receives = defaultReceive :: receives

	final protected def registerReceive(receive: Receive) {
		receives = receive :: receives
		buf = null
	}

	override def receive = {
		case msg =>
			if (buf == null) {
				buf = receives reduce (_ orElse _)
			}
			buf(msg)
	}
}

class MyComposableActor extends ComposableActor {

	import logger._

	override def preStart() {
		registerReceive({
			case "foo" => info("foo")
		})
		registerReceive({
			case "bar" => info("bar")
		})
	}
}

object MyComposableActor extends App {
	val system = ActorSystem("ComposableSystem")
	val actor = system.actorOf(Props[MyComposableActor], "myComposableActor")

	TimeUnit.SECONDS.sleep(1)

	actor ! "foo"
	actor ! "bar"
	actor ! "aaa"

	system.shutdown()
}