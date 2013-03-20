package misty.practices.akka.remoting

import com.typesafe.config.ConfigFactory
import akka.actor.{Props, ActorSystem}
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午2:41
 */
object LookupApp extends App {
	val config = ConfigFactory.load.getConfig("remotelookup")
	val system = ActorSystem("LookupApplication", config)
	val actor = system.actorOf(Props[LookupActor], "lookupActor")
	val remoteActor = system.actorFor("akka://CalculatorApplication@127.0.0.1:2555/user/simpleCalculator")

	doSomething(Add(1, 2))

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	def doSomething(op: MathOp) = {
		actor !(remoteActor, op)
	}
}
