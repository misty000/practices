package misty.classdump

import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-12
 * Time: 下午1:40
 */
object ClassDumpApp extends App {
	val logger = LoggerFactory.getLogger(classOf[ClassDumpApp])
	ClassDump.dump(classOf[Domain])

	logger debug "======= 华丽的分割线 ======="

	val cclz = ClassDump.dump(classOf[ChildDomain])
	println(cclz)

	//	val beanInfo = Introspector.getBeanInfo(classOf[ChildDomain])
	//	println(beanInfo)
	//	beanInfo.getPropertyDescriptors foreach println
	//	beanInfo.getMethodDescriptors foreach println
}

class ClassDumpApp

case class CClass(name: String, pkg: String = "", fields: Seq[CField] = Seq(), methods: Seq[CMethod] = Seq(), superClass: CClass = null) {
	private def fieldsToString = if (fields.isEmpty) "" else fields.mkString("\t", "\n\t", "\n")

	private def methodsToString = methods.mkString("\t", "\n\t", "\n")

	private def superClassToString: String = if (superClass == null) "" else "extends " + superClass.toString

	override def toString = {
		s"[Class] $name: $pkg\n$fieldsToString$methodsToString$superClassToString"
	}
}

case class CField(name: String, typ: Class[_]) {
	private def fieldTypeToString = typ.getName

	override def toString = s"[Field] $name: $fieldTypeToString"
}

case class CMethod(name: String, parameterTypes: Seq[Class[_]] = Seq(), returnType: Class[_] = null) {
	private def paramTypesToString = {
		parameterTypes.length match {
			case 0 => ""
			case 1 => s"(${parameterTypes(0).getName})"
			case _ => s"(${parameterTypes map (_.getName) mkString (", ")})"
		}
	}

	private def returnTypeToString = {
		if (returnType == null || returnType == Void.TYPE || returnType == classOf[Void]) "Unit"
		else returnType.getName
	}

	override def toString = s"[Method] $name: $paramTypesToString => $returnTypeToString"
}

object ClassDump {
	private val logger = LoggerFactory.getLogger(classOf[ClassDump])

	def dump(clazz: Class[_]): CClass = {
		if (clazz == null || clazz == classOf[Object]) null
		else {

			val name = clazz.getSimpleName
			val pkg = clazz.getPackage.getName
			logger debug name
			logger debug pkg

			val fields = analyseFields(clazz)
			val methods = analyseMethods(clazz)
			val superClass = dump(clazz.getSuperclass)

			CClass(name, pkg, fields, methods, superClass)
		}
	}

	private def analyseFields(clazz: Class[_]): Seq[CField] = {
		logger debug "==> fields"
		clazz.getDeclaredFields map (f => {
			logger debug s"field $f"
			val name = f.getName
			val typ = f.getType
			val c = CField(name, typ)
			logger info c.toString
			c
		})
	}

	private def analyseMethods(clazz: Class[_]): Seq[CMethod] = {
		logger debug "==> methods"
		clazz.getDeclaredMethods map (m => {
			logger debug s"method $m"
			val name = m.getName
			val paramTypes = m.getParameterTypes.toSeq
			val returnType = m.getReturnType
			val c = CMethod(name, paramTypes, returnType)
			logger info c.toString
			c
		})
	}

}

class ClassDump

class Domain {
	var name: String = _
	var age: Int = _
}

class ChildDomain extends Domain {
	var addresses: Iterable[Address] = _

	def addresses_=(addr: Address) {

	}

	private var _city: String = _

	def city = _city

	def city_=(city: String) = _city = city

	def test(a: Int, b: List[String]) = ???
}

case class Address(addr: String)