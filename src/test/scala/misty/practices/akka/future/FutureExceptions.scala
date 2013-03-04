package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.duration.DurationDouble
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午9:27
 */
object FutureExceptions extends App {
	{
		val future = Future {
			1
		} recover {
			case e: ArithmeticException => 0
		}
		future foreach println
		Await.ready(future, 5 seconds)
	}

	{
		val future = Future {
			2
		} recoverWith {
			case e: ArithmeticException => Future.successful(0)
			case foo: IllegalArgumentException => Future.failed[Int](new IllegalStateException("All brOken!"))
		}
		future foreach println
		Await.ready(future, 5 seconds)
	}
}
