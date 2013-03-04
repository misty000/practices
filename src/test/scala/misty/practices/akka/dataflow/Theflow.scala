package misty.practices.akka.dataflow

import akka.dataflow._
import java.util.concurrent.TimeUnit
import concurrent.ExecutionContext.Implicits.global
import concurrent.{Future, Promise}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 上午10:51
 */
object Theflow extends App {
	/*
	 * 1、定义数据流变量
	 * val x = Promise[Int]()
	 * val y = Future { 1 }
	 * 
	 * 2、等待数据流变量（必须包含在flow块中）
	 * x()
	 * 
	 * 3、绑定数据流变量（必须包含在flow块中）
	 * x << 3
	 *
	 * 4、数据流变量与Future绑定（必须包含在flow块中）
	 * x << y
	 *
	 * 一个数据流变量只能绑定一次，后续绑定将被忽略
	 */

	flow {
		"1 - Hello world!"
	} onComplete println

	flow {
		val f1 = flow {
			"2 - Hello"
		}
		f1() + " world!"
	} onComplete println

	flow {
		val f1 = flow {
			"Hello"
		}
		val f2 = flow {
			"world!"
		}
		"3 - " + f1() + " " + f2()
	} onComplete println

	{
		val v1, v2 = Promise[Int]()
		flow {
			// (1)由于v2未绑定值，v2()会阻塞
			v1 << v2() + 10
			v1() + v2()
		} onComplete println
		flow {
			// (2)v2获得值，(1)继续执行
			v2 << 5
		}
	}

	{
		val f1, f2 = Future(1)
		val usingFor = for (v1 <- f1; v2 <- f2) yield v1 + v2
		val usingFlow = flow(f1() + f2())
		usingFor onComplete println
		usingFlow onComplete println
	}

	println("wait")
	TimeUnit.SECONDS.sleep(10)
}
