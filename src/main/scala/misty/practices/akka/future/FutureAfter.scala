package misty.practices.akka.future

import akka.pattern
import concurrent.duration.DurationDouble
import akka.actor.ActorSystem
import concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午10:00
 */
object FutureAfter extends App {
	val system = ActorSystem("ActorSystem")
	val delayed = pattern.after[Int](200 millis, using = system.scheduler) {
		Future.failed(new IllegalStateException("OHNOES"))
	}
	val future = Future {
		Thread.sleep(1000)
		2
	}
	val result = Future firstCompletedOf Seq(future, delayed)
	result foreach println
	Await.ready(result, 5 seconds)
	system.shutdown()
}
