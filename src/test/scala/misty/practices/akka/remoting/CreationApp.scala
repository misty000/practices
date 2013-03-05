package misty.practices.akka.remoting

import com.typesafe.config.ConfigFactory
import akka.actor.{Props, ActorSystem}
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午3:12
 */
object CreationApp extends App {
	val config = ConfigFactory.load.getConfig("remotecreation")
	val system = ActorSystem("RemoteCreation", config)
	val localActor = system.actorOf(Props[CreationActor], "creationActor")
	val remoteActor = system.actorOf(Props[AdvancedCalculatorActor], "advancedCalculator")

	doSomething(Multiplication(2, 3))

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	def doSomething(op: MathOp) = {
		localActor !(remoteActor, op)
	}
}
