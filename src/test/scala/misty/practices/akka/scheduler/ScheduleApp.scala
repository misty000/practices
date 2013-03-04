package misty.practices.akka.scheduler

import akka.actor.{Actor, Props, ActorSystem}
import compat.Platform
import concurrent.duration.DurationLong
import concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午3:05
 */
object ScheduleApp extends App {
	val system = ActorSystem("ScheduleSystem")
	val testActor = system.actorOf(Props(new Actor {
		def receive = {
			case m => println(m)
		}
	}))

	system.scheduler.scheduleOnce(50 millis, testActor, "foo")
	system.scheduler.scheduleOnce(50 millis) {
		testActor ! Platform.currentTime
	}

	val Tick = "tick"
	val tickActor = system.actorOf(Props(new Actor {
		def receive = {
			case Tick => println(Tick)
		}
	}))
	val cancellable = system.scheduler.schedule(0 millis, 50 millis, tickActor, Tick)

	TimeUnit.SECONDS.sleep(1)
	cancellable.cancel()

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()
	system.awaitTermination()
}