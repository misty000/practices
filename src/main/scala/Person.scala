import scalafx.beans.property.StringProperty

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-20
 * Time: 上午1:39
 */
class Person {
	var name = new StringProperty(this, "name", "default")
}