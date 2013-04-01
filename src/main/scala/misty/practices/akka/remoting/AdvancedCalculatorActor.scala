package misty.practices.akka.remoting

import akka.actor.Actor

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午3:17
 */
class AdvancedCalculatorActor extends Actor {
	def receive = {
		case Multiplication(n1, n2) => {
			println(s"Calculating $n1 * $n2")
			sender ! MultiplicationResult(n1, n2, n1 * n2)
		}
		case Division(n1, n2) => {
			println(s"Calculating $n1 / $n2")
			sender ! DivisionResult(n1, n2, n1 / n2)
		}
	}
}
