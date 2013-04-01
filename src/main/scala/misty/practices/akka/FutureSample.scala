package misty.practices.akka

import concurrent._
import concurrent.ExecutionContext.Implicits.global
import duration.DurationLong

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午4:36
 */
object FutureSample extends App {
	val f = Future {
		1 + 2
	}
	f onSuccess {
		case i => println("aaa " + i)
	}
	println(Await.result(f, 5 seconds))
}
