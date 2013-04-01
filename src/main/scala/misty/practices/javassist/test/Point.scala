package misty.practices.javassist.test

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-2-28
 * Time: 下午8:42
 */
case class Point(var x: Int, var y: Int) {
	def this() = this(0, 0)

	def move(x: Int, y: Int) {
		this.x += x
		this.y += y
	}
}
