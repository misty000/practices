package org.stairwaybook.recipe

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-13
 * Time: 下午8:19
 */
class Recipe(
				val name: String,
				val ingredients: List[Food],
				val instructions: String
				) {

	override def toString = name
}

object FruitSalad extends Recipe(
	"fruit salad",
	List(Apple, Orange, Cream, Sugar),
	"Stir it all together."
)
