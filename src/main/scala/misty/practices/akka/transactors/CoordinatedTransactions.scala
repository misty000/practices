package misty.practices.akka.transactors

import akka.actor.{Props, ActorSystem, Actor, ActorRef}
import concurrent.stm._
import akka.transactor.Coordinated
import akka.util.Timeout
import akka.pattern.ask
import concurrent.duration.DurationInt
import concurrent.Await

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-6
 * Time: 下午4:45
 */
object CoordinatedTransactions extends App {
	val system = ActorSystem("CoordinatedTransactions")
	val counter1 = system.actorOf(Props[Counter], "counter1")
	val counter2 = system.actorOf(Props[Counter], "counter2")
	implicit val timeout: Timeout = 5.seconds
	counter1 ! Coordinated(Increment(Some(counter2)))
	val count1 = Await.result(counter1 ? GetCount, timeout.duration)
	println(count1)
	val count2 = Await.result(counter2 ? GetCount, timeout.duration)
	println(count2)
	system.shutdown()

	case class Increment(friend: Option[ActorRef] = None)

	case object GetCount

	class Counter extends Actor {
		val count = Ref(0)

		def receive = {
			case coordinated@Coordinated(Increment(friend)) => {
				friend foreach (_ ! coordinated(Increment()))
				coordinated atomic {
					implicit t => count transform (_ + 1)
				}
			}
			case GetCount => sender ! count.single.get
		}
	}

}
