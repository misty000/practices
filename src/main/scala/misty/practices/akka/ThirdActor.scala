package misty.practices.akka

import akka.actor.{ActorSystem, Props, Actor}
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午11:37
 */
case class ImmutableMessage()

case class DoIt(msg: ImmutableMessage) {
	def this() = this(new ImmutableMessage)
}

class ThirdActor extends Actor {
	override def receive = {
		case m: DoIt =>
			println("ThirdActor#receive")
			context.actorOf(Props(new Actor {
				def receive = {
					case DoIt(msg) =>
						println("InnerActor#receive")
						val replyMsg = doSomeDangerousWork(msg)
						println(sender)
						sender ! replyMsg
						context.stop(self)
				}

				def doSomeDangerousWork(msg: ImmutableMessage): String = "done"
			})) ! m
		case e => println(e)
	}
}

object ThirdActor extends App {
	val system = ActorSystem("actorSystem")
	val actor = system.actorOf(Props[ThirdActor])
	actor ! new DoIt
	TimeUnit.SECONDS.sleep(5)
	system.shutdown()
}