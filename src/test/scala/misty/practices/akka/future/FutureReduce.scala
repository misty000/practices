package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration.DurationLong

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午8:42
 */
object FutureReduce extends App {
	val futures = (1 to 1000) map (i => Future(i * 2))
	val futureSum = Future.reduce(futures)(_ + _)
	futureSum foreach println
	Await.ready(futureSum, 5 seconds)
}
