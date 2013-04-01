package misty.practices.akka

import akka.actor.Actor

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午11:22
 */
class SecondActor(val name: String) extends Actor {
	override def receive = {
		case _ =>
	}
}
