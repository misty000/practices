package misty.practices.akka.routing

import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern._
import akka.routing._
import annotation.tailrec
import java.util.concurrent.TimeUnit
import concurrent.duration.DurationDouble
import akka.util.Timeout
import concurrent.Await

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午4:13
 */
object RouterUsage extends App {
	val system = ActorSystem("RouterUsage")

	{
		val routeRobinRouter = system.actorOf(Props[PrintlnActor].withRouter(RoundRobinRouter(5)), "routeRobinRouter")
		1 to 10 foreach {
			i => routeRobinRouter ! i
		}
	}
	TimeUnit.SECONDS.sleep(1)
	println("============")

	{
		val randomRouter = system.actorOf(Props[PrintlnActor].withRouter(RandomRouter(5)), "randomRouter")
		1 to 10 foreach {
			i => randomRouter ! i
		}
	}
	TimeUnit.SECONDS.sleep(1)
	println("============")

	{
		val smallestMailboxRouter = system.actorOf(Props[PrintlnActor].withRouter(SmallestMailboxRouter(5)), "smallestMailboxRouter")
		1 to 10 foreach {
			i => smallestMailboxRouter ! i
		}
	}
	TimeUnit.SECONDS.sleep(1)
	println("============")

	{
		val broadcastRouter = system.actorOf(Props[PrintlnActor].withRouter(BroadcastRouter(5)), "broadcastRouter")
		broadcastRouter ! "this is a broadcast message"
	}
	TimeUnit.SECONDS.sleep(1)
	println("============")

	{
		val scatterGatherFirstCompletedRouter = system.actorOf(Props[FibonacciActor]
			.withRouter(ScatterGatherFirstCompletedRouter(nrOfInstances = 5, within = 2.seconds)),
			"scatterGatherFirstCompletedRouter")
		implicit val timeout: Timeout = 5.seconds
		val futureResult = scatterGatherFirstCompletedRouter ? FibonacciNumber(10)
		val result = Await.result(futureResult, timeout.duration)
		println(s"The result of calculating Fibonacci for 10 is $result")
	}
	TimeUnit.SECONDS.sleep(1)
	println("============")

	system.shutdown()

	class PrintlnActor extends Actor {
		def receive = {
			case msg => println(s"Received message '$msg' in actor ${self.path.name}")
		}
	}

	case class FibonacciNumber(nbr: Int)

	class FibonacciActor extends Actor {
		def receive = {
			case FibonacciNumber(nbr) => sender ! fibonacci(nbr)
		}

		private def fibonacci(n: Int): Int = {
			@tailrec
			def fib(n: Int, b: Int, a: Int): Int = n match {
				case 0 => a
				case _ => fib(n - 1, a + b, b)
			}
			fib(n, 1, 0)
		}
	}

}
