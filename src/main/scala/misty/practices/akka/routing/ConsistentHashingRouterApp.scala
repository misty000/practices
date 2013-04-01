package misty.practices.akka.routing

import akka.actor.{Props, ActorSystem, Actor}
import akka.routing.ConsistentHashingRouter.{ConsistentHashableEnvelope, ConsistentHashable, ConsistentHashMapping}
import akka.routing.ConsistentHashingRouter
import akka.pattern._
import java.util.concurrent.TimeUnit
import akka.util.Timeout
import concurrent.duration.DurationInt
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午5:12
 */
object ConsistentHashingRouterApp extends App {
	val system = ActorSystem("ConsistentHashingRouterApp")
	val cache = system.actorOf(Props[Cache].withRouter(ConsistentHashingRouter(10, hashMapping = hashMapping)))
	implicit val timeout: Timeout = 5.seconds

	cache ! ConsistentHashableEnvelope(message = Entry("hello", "HELLO"), hashKey = "hello")
	cache ! ConsistentHashableEnvelope(message = Entry("hi", "HI"), hashKey = "hi")

	cache ? Get("hello") foreach println
	cache ? Get("hi") foreach println

	cache ! Evict("hi")
	cache ? Get("hi") foreach println

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	def hashMapping: ConsistentHashMapping = {
		case Evict(key) => key
	}

	class Cache extends Actor {
		var cache = Map.empty[String, String]

		def receive = {
			case Entry(key, value) => cache += (key -> value)
			case Get(key) => sender ! cache.get(key)
			case Evict(key) => cache -= key
		}
	}

	case class Evict(key: String)

	case class Get(key: String) extends ConsistentHashable {
		override def consistentHashKey: Any = key
	}

	case class Entry(key: String, value: String)

}
