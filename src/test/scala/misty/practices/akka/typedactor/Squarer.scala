package misty.practices.akka.typedactor

import concurrent._
import akka.actor.{ActorSystem, TypedActor, TypedProps}
import duration.DurationDouble
import ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午3:16
 */
object SquarerApp extends App {
	val system = ActorSystem("SquarerSystem")
	val mySquarer: Squarer = TypedActor(system).typedActorOf(TypedProps[SquarerImpl])
	val otherSquarer: Squarer = TypedActor(system).typedActorOf(TypedProps(classOf[Squarer], new SquarerImpl("foo")), "name")

	println(Thread.currentThread())

	mySquarer.squareDontCare(10)

	val oSquare = mySquarer.squareNowPlease(10)
	println(oSquare)

	val iSquare = mySquarer.squareNow(10)
	println(iSquare)

	val fSquare: Future[Int] = mySquarer.square(10)
	fSquare onSuccess {
		case i: Int => println(i)
	}
	Await.result(fSquare, 5 seconds)

	TypedActor(system).stop(mySquarer)
	TypedActor(system).poisonPill(otherSquarer)

	system.shutdown()
	system.awaitTermination()
}

trait Squarer {
	def squareDontCare(i: Int): Unit //fire-forget

	def square(i: Int): Future[Int] //非阻塞 send-request-reply

	def squareNowPlease(i: Int): Option[Int] //阻塞 send-request-reply

	def squareNow(i: Int): Int //阻塞的 send-request-reply
}

class SquarerImpl(val name: String) extends Squarer {
	def this() = this("default")


	def squareDontCare(i: Int) {
		println("squareDontCare " + Thread.currentThread())
		println(i * i)
	}

	def square(i: Int): Future[Int] = {
		println("square " + Thread.currentThread())
		Promise.successful(i * i).future
	}

	def squareNowPlease(i: Int): Option[Int] = {
		println("squareNowPlease " + Thread.currentThread())
		Some(i * i)
	}

	def squareNow(i: Int): Int = {
		println("squareNow " + Thread.currentThread())
		i * i
	}
}