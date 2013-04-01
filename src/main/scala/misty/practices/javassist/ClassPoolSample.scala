package misty.practices.javassist

import javassist.ClassPool

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 上午10:54
 */
object ClassPoolSample {
	def main(args: Array[String]) {
		val pool = new ClassPool(true)
		val cc = pool.get("misty.practices.javassist.test.Point")
		cc.toClass
		val cc2 = pool.getAndRename("misty.practices.javassist.test.Point", "test.Pair")
		println(cc2.toClass)
	}
}
