package misty.practices.akka.remoting

import akka.actor.{ActorRef, Actor}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午2:27
 */
class LookupActor extends Actor {
	def receive = {
		case (actor: ActorRef, op: MathOp) => actor ! op
		case result: MathResult => result match {
			case AddResult(n1, n2, r) =>
				println(s"Add result: $n1 + $n2 = $r")
			case SubtractResult(n1, n2, r) =>
				println(s"Sub result: $n1 - $n2 = $r")
		}
	}
}
