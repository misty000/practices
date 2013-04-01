package misty.practices.javassist

import javassist.{Loader, ClassPool}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午12:47
 */
object UsingJavassistLoaderSample2 {
	def main(args: Array[String]) {
		val t = new MyTranslator
		val pool = ClassPool.getDefault
		val cl = new Loader
		cl.addTranslator(pool, t)
		cl.run("test.MyApp", args)
	}
}
