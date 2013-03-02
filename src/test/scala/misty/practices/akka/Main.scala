package misty.practices.akka

import akka.actor.{Props, ActorSystem}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午11:00
 */
object Main extends App {
	val system = ActorSystem("MySystem")
	val myActor = system.actorOf(Props[MyActor], "myactor")
	myActor ! "test"
	myActor ! "unknown"

	system.actorOf(Props(new SecondActor("SecondActor")), "secondActor")

	val props1 = Props()
	val props2 = Props[MyActor]
	val props3 = Props(new MyActor)
	val props4 = Props(creator = {
		() => new MyActor
	}, dispatcher = "my-dispatcher")
	val props5 = props1.withCreator(new MyActor)
	val props6 = props5.withDispatcher("my-dispatcher")

	val myActor2 = system.actorOf(Props[MyActor].withDispatcher("my-dispatcher"), "myactor2")


	system.shutdown()
}
