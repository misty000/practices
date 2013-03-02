package misty.practices.akka

import concurrent.duration.{fromNow, DurationInt}
import org.scalatest.FlatSpec
import actors.threadpool.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午8:57
 */
class DurationSample extends FlatSpec {
	it should "5.sec - 3.millis < 5.sec" in {
		val fivesec = 5.seconds
		val threemillis = 3.millis
		val diff = fivesec - threemillis
		assert(diff < fivesec)
		val fourmillis = threemillis * 4 / 3
		println(fourmillis)
		val n = threemillis / (1 millis)
	}

	it should "left sometime from now" in {
		val deadline = 10 seconds fromNow
		TimeUnit.SECONDS.sleep(5)
		println(deadline.timeLeft)
	}
}
