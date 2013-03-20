package org.stairwaybook.recipe

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-13
 * Time: 下午8:25
 */
object SimpleDatabase {
	def allFoods = List(Apple, Orange, Cream, Sugar)

	def foodNamed(name: String): Option[Food] = allFoods.find(_.name == name)

	def allRecipes: List[Recipe] = List(FruitSalad)
}

object SimpleBrowser {
	def recipesUsing(food: Food) = SimpleDatabase.allRecipes.filter(recipe => {
		recipe.ingredients.contains(food)
	})
}