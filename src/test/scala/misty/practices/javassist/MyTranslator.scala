package misty.practices.javassist

import javassist.{ClassPool, Translator}
import java.lang.reflect.Modifier

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午12:45
 */
class MyTranslator extends Translator {
	def start(pool: ClassPool) {
		println("start")
	}

	def onLoad(pool: ClassPool, classname: String) {
		println(classname + " onLoad")
		val cc = pool.get(classname)
		cc.setModifiers(Modifier.PUBLIC)
	}
}
