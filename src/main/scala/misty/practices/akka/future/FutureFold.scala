package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.duration.DurationLong
import concurrent.ExecutionContext.Implicits.global
import collection.mutable
import java.lang.Thread

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午12:39
 */
object FutureFold extends App {
	val threadSet = mutable.Set[Thread]()
	val futures = (1 to 1000).map(n => Future(n * 2))
	val futureSum = Future.fold(futures)(0) {
		(x, y) => {
			threadSet += Thread.currentThread()
			x + y
		}
	}
	futureSum foreach println
	Await.ready(futureSum, 5 seconds)
	println(threadSet)
}
