package misty.practices.akka.future

import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午11:42
 */
object FutureSequence2 extends App {
	var n = 1;
	val futureList = List.fill(100) {
		Future {
			val r = n
			n += 2
		}.mapTo[Int]
	}
	futureList foreach println
	println("===")
	val listFuture = Future.sequence(futureList)
	println(listFuture)
	listFuture foreach println
	val oddSum = listFuture map (_.sum)
	println(oddSum)
	oddSum foreach println
	TimeUnit.SECONDS.sleep(10)
}
