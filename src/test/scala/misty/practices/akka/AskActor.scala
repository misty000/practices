package misty.practices.akka

import akka.actor.{Props, ActorSystem, Actor}
import concurrent.duration.DurationDouble
import akka.util.Timeout
import akka.pattern._
import concurrent.Future

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 上午1:18
 */
object AskActor extends App {

	case class Result(x: Int, s: String, d: Double)

	case object Request

	implicit val timeout = Timeout(5.seconds)

	val system = ActorSystem("ActorSystem")
	val actorA = system.actorOf(Props[MyActor], "actorA")
	val actorB = system.actorOf(Props[MyActor], "actorB")
	val actorC = system.actorOf(Props[MyActor], "actorC")
	val actorD = system.actorOf(Props[MyActor], "actorD")

//	val f: Future[Result] =
//		for {
//			x ← ask(actorA, Request).mapTo[Int] // 直接调用
//			s ← actorB ask Request mapTo manifest[String] // 隐式转换调用
//			d ← actorC ? Request mapTo manifest[Double] // 通过符号名调用
//		} yield Result(x, s, d)
//
//	f pipeTo actorD // .. 或 ..
//	pipe(f) to actorD

	class MyActor extends Actor {
		override def receive = {
			case msg => println(msg)
		}
	}

}