package misty.practices.akka.transactors

import akka.transactor.{Coordinated, Transactor}
import concurrent.stm.Ref
import akka.actor.{Props, ActorSystem, ActorRef}
import akka.util.Timeout
import concurrent.duration.DurationDouble
import concurrent.Await
import akka.pattern.ask

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-6
 * Time: 下午5:38
 */
object Transactors extends App {

	implicit val system = ActorSystem("Transactors")
	val actor1 = system.actorOf(Props[Counter], "counter")
	val actor2 = system.actorOf(Props(new FriendlyCounter(actor1)), "friendlyCounter")
	implicit val timeout: Timeout = 5.seconds

	actor2 ! Coordinated(Increment)

	val count2 = Await.result(actor2 ? GetCount, timeout.duration)
	println(count2)
	val count1 = Await.result(actor1 ? GetCount, timeout.duration)
	println(count1)
	system.shutdown()

	case object Increment

	case object GetCount

	class Counter extends Transactor {
		val count = Ref(0)

		override def atomically = implicit txn => {
			case Increment => {
				println(s"${self.path.name} atomically")
				count transform (_ + 1)
			}
			case GetCount => {
				sender ! count.single.get
			}
		}
	}

	class FriendlyCounter(friend: ActorRef) extends Transactor {
		val count = Ref(0)

		override def coordinate = {
			case Increment => {
				println(s"friendlyCounter coordinate: $friend")
				//include(actor1, actor2, actor3)
				include(friend)
			}
		}

		override def atomically = implicit txn => {
			case Increment => {
				println(s"${self.path.name} atomically")
				count transform (_ + 1)
			}
			case GetCount => {
				sender ! count.single.get
			}
		}
	}

}
