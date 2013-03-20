package misty.practices.akka.logging

import akka.actor._
import akka.event.Logging
import com.typesafe.config.ConfigFactory

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午5:54
 */
object MyActor extends App {
	// akka.loglevel = DEBUG
	val config = ConfigFactory.parseString(
		"""
		  |akka {
		  |  loglevel = DEBUG
		  |  debug {
		  |    receive = on
		  |    autoreceive = on
		  |    lifecycle = on
		  |    fsm = on
		  |  }
		  |}
		""".stripMargin)
	val system = ActorSystem("System", config)
	val actor = system.actorOf(Props[MyActor])
	actor ! "test"
	actor ! "unknown"

	actor ! Kill
	system.shutdown()
}

class MyActor extends Actor {
	val log = Logging(context.system, this)

	override def preStart() {
		log.debug("Starting")
	}

	override def preRestart(reason: Throwable, msg: Option[Any]) {
		log.error(reason, "Restarting due to [{}] when processing [{}]", reason.getMessage, msg.getOrElse(""))
	}

	override def receive = {
		case "test" => log.info("Received test")
		case x => log.warning("Received unknown message: {}", x)
	}
}

class MyActor2 extends Actor with ActorLogging {
	override def receive = {
		case "test" => log.info("Received test")
		case x => log.warning("Received unknown message: {}", x)
	}
}