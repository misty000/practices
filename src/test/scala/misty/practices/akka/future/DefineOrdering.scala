package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import util.Failure
import concurrent.duration.DurationDouble

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午9:01
 */
object DefineOrdering extends App {
	val url = "aaa"
	val result = Future {
		loadPage(url)
	} andThen {
		case Failure(exception) => {
			log(exception)
		}
	} andThen {
		case _ => watchSomeTV()
	}
	result foreach println
	Await.ready(result, 5 seconds)


	def loadPage(url: String) = throw new RuntimeException("RuntimeException")

	def log(exception: Throwable) {
		exception.printStackTrace()
	}

	def watchSomeTV() {
		println("watchSomeTV")
	}
}
