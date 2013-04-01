package misty.practices.akka.future

import concurrent.{Await, Future}
import concurrent.duration.DurationInt
import concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午9:20
 */
object FutureAuxiliaryMethods extends App {
	// Future fallbackTo 将两个 Futures 合并成一个新的 Future, 如果第一个Future失败了，它将持有第二个 Future 的成功值。
	val future1 = Future {
		1
	}
	val future2 = Future {
		2
	}
	val future3 = Future {
		3
	}
	val future4 = future1 fallbackTo future2 fallbackTo future3
	future4 foreach println

	// 你也可以使用zip操作将两个 Futures 组合成一个新的持有二者成功结果的tuple的 Future
	val future5 = future1 zip future2 map {
		case (a, b) => a + " " + b
	}
	future5 foreach println

	Await.ready(future4, 5 seconds)
	Await.ready(future5, 5 seconds)
}
