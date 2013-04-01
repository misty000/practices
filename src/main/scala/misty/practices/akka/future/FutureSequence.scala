package misty.practices.akka.future

import akka.actor.{Actor, Props, ActorSystem}
import concurrent.{Await, Future}
import concurrent.duration.DurationDouble
import akka.util.Timeout
import concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午10:02
 */
object FutureSequence {

	case object GetNext

	def main(args: Array[String]) {
		println("~")
		val system = ActorSystem("FutureSequenceSystem")
		val oddActor = system.actorOf(Props(new Actor {
			private var i = 1

			def receive = {
				case GetNext =>
					val r = i
					println(s"actor $r")
					i += 2
					sender ! r
			}
		}))
		implicit val timeout: Timeout = 10.seconds

		// oddActor returns odd numbers sequentially from 1 as a List[Future[Int]]
		val listOfFutures = List.fill(100)(akka.pattern.ask(oddActor, GetNext).mapTo[Int])

		// now we have a Future[List[Int]]
		val futureList = Future.sequence(listOfFutures)

		// Find the sum of the odd numbers
		val oddSum = futureList.map(_.sum)
		oddSum foreach println

		println("!")
		//		Await.ready(oddSum, 500 seconds)
		Await.ready(oddSum, 500 seconds)
		system.shutdown()
	}
}
