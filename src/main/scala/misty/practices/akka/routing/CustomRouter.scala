package misty.practices.akka.routing

import akka.routing.{RouteeProvider, RouterConfig, Route}
import akka.dispatch.Dispatchers
import akka.actor._
import akka.routing.Destination
import akka.pattern._
import java.util.concurrent.TimeUnit
import akka.util.Timeout
import concurrent.duration.DurationLong
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午6:35
 */
object CustomRouter extends App {
	val system = ActorSystem("CustomRouter")
	val router = system.actorOf(Props(new Actor {
		def receive = {
			case m => println(m)
		}
	}).withRouter(VoteCountRouter()), "customRouter")
	println(router.path)
	implicit val timeout: Timeout = 5.seconds

	router ! DemocratVote
	router ! RepublicanVote
	router ! DemocratVote
	router ! DemocratVote
	router ! DemocratVote
	router ! RepublicanVote
	router ! DemocratVote
	router ! DemocratVote
	router ! RepublicanVote
	router ! RepublicanVote
	router ! RepublicanVote

	router ? DemocratCountResult foreach println
	router ? RepublicanCountResult foreach println

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	case object DemocratVote

	case object DemocratCountResult

	case object RepublicanVote

	case object RepublicanCountResult

	class DemocratActor extends Actor {
		var counter = 0

		def receive = {
			case DemocratVote => counter += 1
			case DemocratCountResult => sender ! counter
		}
	}

	class RepublicanActor extends Actor {
		var counter = 0

		def receive = {
			case RepublicanVote => counter += 1
			case RepublicanCountResult => sender ! counter
		}
	}

	case class VoteCountRouter() extends RouterConfig {
		def routerDispatcher: String = Dispatchers.DefaultDispatcherId

		def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy

		def createRoute(routeeProvider: RouteeProvider): Route = {
			val democratActor = routeeProvider.context.actorOf(Props(new DemocratActor()), "d")
			val republicanActor = routeeProvider.context.actorOf(Props(new RepublicanActor()), "r")
			val routees = Vector[ActorRef](democratActor, republicanActor)
			routeeProvider.registerRoutees(routees)

			{
				case (sender, msg) => msg match {
					case DemocratVote | DemocratCountResult => List(Destination(sender, democratActor))
					case RepublicanVote | RepublicanCountResult => List(Destination(sender, republicanActor))
				}
			}
		}
	}

}