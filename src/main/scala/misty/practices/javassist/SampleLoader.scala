package misty.practices.javassist

import javassist.ClassPool

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午1:17
 */
object SampleLoader {
	def main(args: Array[String]) {
		val s = new SampleLoader
		val c = s.loadClass("test.MyApp")
		c.getDeclaredMethod("main", classOf[Array[String]]).invoke(null, args: _*)
	}
}

class SampleLoader extends ClassLoader {
	private val pool:ClassPool = new ClassPool
	pool.insertClassPath("./class")

}
