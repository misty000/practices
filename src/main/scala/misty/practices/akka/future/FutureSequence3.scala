package misty.practices.akka.future

import akka.actor.{Props, ActorSystem}
import misty.practices.akka.future.OddActor.GetNext
import akka.pattern.ask
import concurrent.duration.DurationDouble
import concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午11:52
 */
object FutureSequence3 extends App {
	val system = ActorSystem("FutureSequence3")
	val actor = system.actorOf(Props[OddActor])
	val futureList = List.fill(100) {
		actor.?(GetNext)(5 seconds).mapTo[Int]
	}

	val listFuture = Future.sequence(futureList)
	val sumFuture = listFuture.map(_.sum)
	sumFuture foreach println

	Await.ready(sumFuture, 5 seconds)

	system.shutdown()
}
