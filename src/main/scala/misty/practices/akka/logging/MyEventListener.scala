package misty.practices.akka.logging

import akka.actor.Actor
import akka.event.Logging._
import akka.event.Logging.InitializeLogger
import akka.event.Logging.Warning

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午6:53
 */
class MyEventListener extends Actor {
	override def receive = {
		case InitializeLogger(_) => sender ! LoggerInitialized
		case Error(cause, logSource, logClass, msg) =>
		case Warning(logSource, logClass, msg) =>
		case Info(logSource, logClass, msg) =>
		case Debug(logSource, logClass, msg) =>
	}
}