package misty.practices.akka.remoting

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午2:23
 */
object SimpleCalculatorApp extends App {
	val config = ConfigFactory.load.getConfig("calculator")
	val system = ActorSystem("CalculatorApplication", config)
	val actor = system.actorOf(Props[SimpleCalculatorActor], "simpleCalculator")
}
