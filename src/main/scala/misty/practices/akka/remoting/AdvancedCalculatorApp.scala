package misty.practices.akka.remoting

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午3:23
 */
object AdvancedCalculatorApp extends App {
	val config = ConfigFactory.load.getConfig("creation")
	val system = ActorSystem("AdvancedCalculatorApplication", config)
}
