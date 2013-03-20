package misty.practices.akka

import akka.actor.{Props, Actor}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午11:08
 */
class FirstActor extends Actor {
	val myActor = context.actorOf(Props[MyActor], "myactor")

	def receive = {
		case _ =>
	}
}
