package misty.practices.javassist

import test.Hello
import javassist.ClassPool

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 上午11:57
 */
object ClassLoaderSample2 {
	def main(args: Array[String]) {
		val orig = new Hello
		val cp = ClassPool.getDefault
		val cc = cp.get("test.Hello")
		val method = cc.getDeclaredMethod("say")
		method.insertBefore( """{System.out.println("Hello.say();");}""")
		cc.toClass(orig.getClass.getClassLoader, orig.getClass.getProtectionDomain)
	}
}
