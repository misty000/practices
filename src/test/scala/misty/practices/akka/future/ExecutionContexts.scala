package misty.practices.akka.future

import concurrent.{Promise, ExecutionContext}

import java.util.concurrent.{TimeUnit, Executors}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午3:40
 */
object ExecutionContexts extends App {
	implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
	val f = Promise.successful("foo")
	ec.shutdown()
	ec.awaitTermination(Int.MaxValue, TimeUnit.SECONDS)
}
