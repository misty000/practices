package misty.practices.akka.dispatchers

import akka.actor.{Props, Actor}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午1:37
 */
object SettingDispatcherForActor extends App {

	class MyActor extends Actor {
		val myActor2 = context.actorOf(Props[MyActor2].withDispatcher("my-dispatcher"), "myactor2")

		def receive = {
			case _ =>
		}
	}

	class MyActor2 extends Actor {
		def receive = {
			case _ =>
		}
	}

}

