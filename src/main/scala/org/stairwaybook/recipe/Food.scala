package org.stairwaybook.recipe

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-13
 * Time: 下午8:18
 */
abstract class Food(val name: String) {
	override def toString = name
}

object Apple extends Food("Apple")

object Orange extends Food("Orange")

object Cream extends Food("Cream")

object Sugar extends Food("Sugar")