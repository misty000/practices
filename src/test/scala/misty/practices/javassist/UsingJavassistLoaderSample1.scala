package misty.practices.javassist

import javassist.{Loader, ClassPool}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午12:29
 */
object UsingJavassistLoaderSample1 {
	def main(args: Array[String]) {
		val pool = ClassPool.getDefault
		val cl = new Loader(pool)

		val ct = pool.get("test.Rectangle")
		ct.setSuperclass(pool.get("test.Point"))

		val c = cl.loadClass("test.Rectangle")
		val rect = c.newInstance()
		println(rect)
		println(rect.getClass.getSuperclass)
	}
}
