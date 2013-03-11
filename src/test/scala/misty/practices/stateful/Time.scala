package misty.practices.stateful

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-9
 * Time: 下午4:41
 */
class Time {
	private[this] var h = 12
	private[this] var m = 0

	def hour: Int = h

	def hour_=(x: Int) {
		require(0 <= x && x < 24)
		h = x
	}

	def minute: Int = m

	def minute_=(x: Int) {
		require(0 <= x && x < 60)
		m = x
	}

}
