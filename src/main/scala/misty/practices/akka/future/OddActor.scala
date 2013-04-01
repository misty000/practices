package misty.practices.akka.future

import akka.actor.Actor
import misty.practices.akka.future.OddActor.GetNext

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午11:50
 */
object OddActor {

	case object GetNext

}

class OddActor extends Actor {
	private var i = 1

	def receive = {
		case GetNext =>
			val r = i
			println(s"actor $r")
			i += 2
			sender ! r
	}
}
