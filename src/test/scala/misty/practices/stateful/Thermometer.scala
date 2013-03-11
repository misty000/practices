package misty.practices.stateful

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-9
 * Time: 下午4:57
 */
class Thermometer {
	var celsiue: Float = _

	def fahrenheit = celsiue * 9 / 5 + 32

	def fahrenheit_=(f: Float) {
		celsiue = (f - 32) * 5 / 9
	}

	override def toString = fahrenheit + "F/" + celsiue + "C"
}
