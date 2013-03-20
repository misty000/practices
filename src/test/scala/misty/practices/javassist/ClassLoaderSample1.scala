package misty.practices.javassist

import javassist.ClassPool
import test.Hello

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 上午10:57
 */
object ClassLoaderSample1 {
	def main(args: Array[String]) {
		{
			val pool = new ClassPool(true)
			val cc = pool.get("test.Hello")
			val method = cc.getDeclaredMethod("say")
			method.insertBefore( """{System.out.println("Hello.say();");}""")
			cc.toClass

			val hello = new Hello
			hello.say()
		}
	}
}
