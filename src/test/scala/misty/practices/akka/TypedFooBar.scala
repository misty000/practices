package misty.practices.akka

import concurrent.{Promise, Future}
import akka.actor.{TypedProps, ActorSystem, TypedActor}
import scala.concurrent.ExecutionContext.Implicits.global
import util.{Failure, Success}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-2
 * Time: 下午5:34
 */
object TypedFooBar extends App {
	val system = ActorSystem("TypedFooBarSystem")
	val awesomeFooBar: Foo with Bar = TypedActor(system).typedActorOf(TypedProps[FooBar])
	awesomeFooBar.doFoo(10)
	val f = awesomeFooBar.doBar("yes")
	f onComplete {
		case Success(s) => println(s)
		case Failure(s) => println(s)
	}
	TypedActor(system).poisonPill(awesomeFooBar)
	system.shutdown()
}

class FooBar extends Foo with Bar

trait Foo {
	def doFoo(times: Int) {
		println("doFoo(" + times + ")")
	}
}

trait Bar {
	def doBar(str: String): Future[String] = Promise.successful(str.toUpperCase).future
}