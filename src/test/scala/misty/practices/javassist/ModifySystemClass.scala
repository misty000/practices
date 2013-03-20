package misty.practices.javassist

import javassist.{CtClass, CtField, ClassPool}
import java.lang.reflect.Modifier

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午1:33
 */
object ModifySystemClass {
	def main(args: Array[String]) {
		val pool = ClassPool.getDefault
		val cc = pool.get("java.lang.String")
		val f = new CtField(CtClass.intType, "hiddenValue", cc)
		f.setModifiers(Modifier.PUBLIC)
		cc.addField(f)
		cc.writeFile()
	}
}
