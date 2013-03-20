package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.duration.DurationDouble
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午11:37
 */
object FutureTraverse extends App {
	val futureList = Future.traverse((1 to 100).toList) {
		x => Future(x * 2 - 1)
	}
	val oddSum = futureList.map(_.sum)
	oddSum foreach println
	Await.ready(oddSum, 10 seconds)
}
