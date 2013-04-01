package misty.practices.javassist

import javassist.{CtClass, CtNewMethod, ClassPool}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午6:55
 */
object AddingMethod {
	def main(args: Array[String]) {
		val pool = ClassPool.getDefault

		pool.importPackage("test")

		val clsPoint = pool.get("misty.practices.javassist.test.Point")
		val m = CtNewMethod.make(
			"""
			  |public void xmove(int dx) {Rectangle r = null;x += dx;}
			""".stripMargin, clsPoint)
		clsPoint.addMethod(m)

		val m2 = CtNewMethod.make(
			"""
			  |public void ymove(int dy) {this.move(0, dy);}
			""".stripMargin, clsPoint)
		clsPoint.addMethod(m2)

		clsPoint.toClass
	}
}
