package misty.practices.akka.future

import concurrent.ExecutionContext.Implicits.global
import concurrent.{Await, Future}
import util.{Failure, Success}
import concurrent.duration.DurationDouble

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午8:52
 */
object FutureCallback extends App {
	val future = Future {
		"bar"
	}
	future onSuccess {
		case "bar" => println("Got my bar alright!")
		case x: String => println("Got some random string: " + x)
	}
	future onFailure {
		case ise: IllegalStateException if ise.getMessage == "OHNOES" =>
		case e: Exception =>
	}
	future onComplete {
		case Success(result) => doSomethingOnSuccess(result)
		case Failure(failure) => doSomethingOnFailure(failure)
	}
	Await.ready(future, 5 seconds)

	def doSomethingOnSuccess(any: Any) = ???

	def doSomethingOnFailure(any: Any) = ???
}
