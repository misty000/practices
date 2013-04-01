package misty.practices.akka.remoting

import akka.actor.{ActorRef, Actor}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午3:06
 */
class CreationActor extends Actor {
	def receive = {
		case (actor: ActorRef, op: MathOp) => actor ! op
		case result: MathResult => result match {
			case MultiplicationResult(n1, n2, r) =>
				println(s"Mul result: $n1 * $n2 = $r")
			case DivisionResult(n1, n2, r) =>
				println(s"Div result: $n1 / $n2 = $r")
		}
	}
}
