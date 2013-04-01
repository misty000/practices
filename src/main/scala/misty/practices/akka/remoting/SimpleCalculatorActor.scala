package misty.practices.akka.remoting

import akka.actor.Actor

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午1:43
 */
class SimpleCalculatorActor extends Actor {
	def receive = {
		case Add(n1, n2) =>
			println(s"Calculating $n1 + $n2")
			sender ! AddResult(n1, n2, n1 + n2)
		case Subtract(n1, n2) =>
			println(s"Calculating $n1 - $n2")
			sender ! SubtractResult(n1, n2, n1 - n2)
	}
}
