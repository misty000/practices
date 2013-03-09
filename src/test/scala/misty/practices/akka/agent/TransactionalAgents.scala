package misty.practices.akka.agent

import akka.agent.Agent
import scala.concurrent.stm._
import akka.util.Timeout
import concurrent.duration.DurationInt
import akka.actor.ActorSystem

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-6
 * Time: 下午3:30
 */
object TransactionalAgents extends App {

	def transfer(from: Agent[Int], to: Agent[Int], amount: Int): Boolean = {
		atomic(txn => {
			if (from.get < amount) false
			else {
				from send (_ - amount)
				to send (_ + amount)
				true
			}
		})
	}

	implicit val system = ActorSystem("TransactionalAgents")

	val from = Agent(100)
	val to = Agent(20)
	val ok = transfer(from, to, 50)
	println(s"before transfer - from: ${from()}, to: ${to()}")

	implicit val timeout: Timeout = 5.seconds
	val fromValue = from.await
	val toValue = to.await
	println(s"after transfer [$ok]- from: $fromValue, to: $toValue")

	from.close()
	to.close()
	system.shutdown()
}
