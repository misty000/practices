package misty.practices.javassist

import javassist.{ByteArrayClassPath, ClassClassPath, CtNewMethod, ClassPool}
import test.Rectangle

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-2-28
 * Time: 下午8:43
 */
object ReadingAndWritingBytecodeSample {
	def main(args: Array[String]) {
		// define a new class
		{
			val pool = new ClassPool(true)
			val cc = pool.get("test.Rectangle")
			cc.setSuperclass(pool.get("test.Point"))
			cc.writeFile()
			// save .class file
			// cc.writeFile()

			// get byte codes
			val byteCodes = cc.toBytecode
			println(byteCodes.mkString("[", ", ", "]"))

			// directly load class
			val cls = cc.toClass
			println(cls)
			println(classOf[Rectangle].getSuperclass)
		}
		{
			val pool = new ClassPool(true)
			val p2 = pool.makeClass("test.Point2")
			val m = CtNewMethod.make(
				"""
				  |public void move(int x, int y) {
				  |	System.out.println("move to " + x + ", " + y);
				  |}
				""".stripMargin, p2)
			p2.addMethod(m)
			println(p2.toClass)

			val newInterface = pool.makeInterface("test.Drawable")
			println(newInterface.toClass)
		}

		// frozen class
		{
			val pool = new ClassPool(true)
			val cc = pool.makeClass("test.Frozen")
			cc.toClass
			try {
				cc.setSuperclass(pool.makeClass("test.FrozenParent"))
			} catch {
				case e: Throwable => e.printStackTrace()
			}
			cc.defrost()
			cc.setSuperclass(pool.makeClass("test"))

			ClassPool.doPruning = true
			val ctCls = pool.makeClass("test.Pruning")
			ctCls.stopPruning(true)
		}

		{
			val pool = new ClassPool(true)
			pool.insertClassPath(new ClassClassPath(this.getClass))

			val b = Array[Byte]()
			val name = "ClassName"
			pool.insertClassPath(new ByteArrayClassPath(name, b))
		}

		{
			val pool = new ClassPool(true)
			val cc = pool.makeClass("test.Detach")
			cc.writeFile()
			cc.detach()
		}

		{
			val cp = new ClassPool(true)
		}

		{
			val parent = ClassPool.getDefault
			val child = new ClassPool(parent)
			child.insertClassPath("./classes")
			child.childFirstLookup = true
		}

		{
			val pool = new ClassPool(true)
			val cc = pool.get("test.Point")
			cc.setName("test.Pair")
			println(cc.toClass)
		}

		{
			val pool = new ClassPool(true)
			val cc = pool.get("test.Point")
			val cc1 = pool.get("test.Point")
			println(cc eq cc1) // true

			cc.setName("test.Pair")
			val cc2 = pool.get("test.Pair")
			println(cc eq cc2) // true

			val cc3 = pool.get("test.Point")
			println(cc eq cc3) // false
		}

		{
			val pool = new ClassPool(true)
			val cc = pool.get("test.Point")
			cc.toClass
			val cc2 = pool.getAndRename("test.Point", "test.Pair")
			println(cc2.toClass)
		}
	}
}
