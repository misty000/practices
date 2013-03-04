package misty.practices.akka.future

import concurrent.{Promise, Await, Future}
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration._

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午5:27
 */
object UseDirectly extends App {
	val future = Future {
		"Hello" + "World"
	}
	val result = Await.result(future, 1 second)

	val f = Promise.successful("Yay!")
	val of = Promise.failed[String](new IllegalArgumentException("Bang!"))
}
