package misty.practices.stateful

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-9
 * Time: 下午4:35
 */
class Keyed {
	def computeKey: Int = ???
}

class MemoKeyed extends Keyed {
	private var keyCache: Option[Int] = None

	override def computeKey: Int = {
		if (!keyCache.isDefined) keyCache = Some(super.computeKey)
		keyCache.get
	}
}
