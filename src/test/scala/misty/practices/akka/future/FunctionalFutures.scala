package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration.DurationDouble
import java.util.concurrent.TimeUnit
import compat.Platform

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-3
 * Time: 下午6:01
 */
object FunctionalFutures extends App {
	{
		val f1 = Future {
			"Hello" + "World"
		}
		val f2 = f1 map {
			x => x.length
		}
		f2 foreach println
		Await.ready(f2, 2 seconds)
	}

	{
		val f1 = Future {
			"Hello" + "World"
		}
		val f2 = Future.successful(3)
		val f3 = f1 map {
			x =>
				println(s"x = $x")
				f2 map {
					y =>
						println(s"y = $y")
						x.length * y
				}
		}
		println(f3)
		f3 foreach (_.foreach(println))
		Await.ready(f3, 1 seconds)
	}

	{
		println("p3")
		println(Platform.currentTime)
		val f1 = Future {
			println("f1")
			"Hello" + "World"
		}
		val f2 = Future {
			println("f2")
			3
		}
		val f3 = f1 flatMap {
			x => f2 map {
				y => x.length * y
			}
		}
		Await.ready(f3, 5 second).foreach(println)
		println(Platform.currentTime)
	}

	{
		TimeUnit.SECONDS.sleep(1)
		println("p4")
		val f1 = Future.successful(4)
		val f2 = f1.filter(_ % 2 == 0)
		f2 foreach println

		val failed = f1.filter(_ % 2 == 1).recover {
			case m: NoSuchElementException => 0
		}
		failed foreach println

		Await.ready(failed, 10 seconds)
	}

	{
		TimeUnit.SECONDS.sleep(1)
		println("p5")
		val f = for {
			a <- Future {
				println("f a")
				TimeUnit.SECONDS.sleep(1)
				println("f a -")
				10 / 2
			}
			b <- Future {
				println("f b")
				TimeUnit.SECONDS.sleep(2)
				println("f b -")
				a + 1
			}
			c <- Future {
				println("f c")
				TimeUnit.SECONDS.sleep(3)
				println("f c -")
				a - 1
			}
			if c > 3
		} yield b * c
		f foreach println
		Await.ready(f, 10 seconds)
	}

	{
		TimeUnit.SECONDS.sleep(1)
		println("f6")
		val fa = Future {
			println("f a")
			TimeUnit.SECONDS.sleep(1)
			println("f a -")
			10 / 2
		}
		val fb = Future {
			println("f b")
			TimeUnit.SECONDS.sleep(2)
			println("f b -")
			10 * 2
		}
	}

	{
		TimeUnit.SECONDS.sleep(1)
		println("f7")
		val f1 = Future {
			println("f1 ~")
			TimeUnit.MILLISECONDS.sleep(300)
			println("f1 =")
			5
		}
		val f2 = Future {
			println("f2 ~")
			TimeUnit.MILLISECONDS.sleep(600)
			println("f2 =")
			8
		}
		val f3 = for {
			a <- f1.mapTo[Int]
			b <- f2.mapTo[Int]
			c <- Future {
				println("f3 ~")
				TimeUnit.MILLISECONDS.sleep(300)
				println("f3 =")
				a + b
			}.mapTo[Int]
		} yield c
		println("!")
		f3 foreach println
		Await.ready(f3, 5 seconds)
	}

	{
		TimeUnit.SECONDS.sleep(1)
		println("f8")
		val f3 = for {
			x <- Future {
				println("fx ~")
				TimeUnit.MILLISECONDS.sleep(300)
				println("fx =")
				10 / 2
			}
			a <- Future {
				println("f1 ~")
				TimeUnit.MILLISECONDS.sleep(300)
				println("f1 =")
				x + 1
			}
			b <- Future {
				println("f2 ~")
				TimeUnit.MILLISECONDS.sleep(600)
				println("f2 =")
				x - 1
			}
			c <- Future {
				println("f3 ~")
				TimeUnit.MILLISECONDS.sleep(300)
				println("f3 =")
				a * b
			}
		} yield c
		println("!")
		f3 foreach println
		Await.ready(f3, 5 seconds)
	}
}
