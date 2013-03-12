package misty.practices.slick._3_lifted

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-11
 * Time: 下午6:09
 */
object LiftedEmbedding extends App {

	object Suppliers extends Table[(Int, String, String, String, String, String)]("SUPPLIERS") {
		def id = column[Int]("SUP_ID", O.PrimaryKey)

		// This is the primary key column
		def name = column[String]("SUP_NAME")

		def street = column[String]("STREET")

		def city = column[String]("CITY")

		def state = column[String]("STATE")

		def zip = column[String]("ZIP")

		// Every table needs a * projection with the same type as the table's type parameter
		def * = id ~ name ~ street ~ city ~ state ~ zip
	}

	object Coffees extends Table[(String, Int, Double, Int, Int)]("COFFEES") {
		def name = column[String]("COF_NAME", O.PrimaryKey)

		def supID = column[Int]("SUP_ID")

		def price = column[Double]("PRICE")

		def sales = column[Int]("SALES", O.Default(0))

		def total = column[Int]("TOTAL", O.Default(0))

		def * = name ~ supID ~ price ~ sales ~ total

		def supplier = foreignKey("SUP_FK", supID, Suppliers)(_.id)
	}

	val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
	db withSession {
		val ddl = Suppliers.ddl ++ Coffees.ddl
		ddl.create

		println("==insert datas")
		// Insert some suppliers
		Suppliers.insert(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
		Suppliers.insert(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460")
		Suppliers.insert(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")

		// Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
		Coffees.insertAll(
			("Colombian", 101, 7.99, 0, 0),
			("French_Roast", 49, 8.99, 0, 0),
			("Espresso", 150, 9.99, 0, 0),
			("Colombian_Decaf", 101, 8.99, 0, 0),
			("French_Roast_Decaf", 49, 9.99, 0, 0)
		)

		{
			val q = Query(Coffees)
			val q1 = q.filter(_.supID === 101)
			val q2 = q.drop(10).take(5)
			val q3 = q.sortBy(_.name.desc.nullsFirst)
			q foreach println
			q1 foreach println
			q2 foreach println
			q3 foreach println
		}

		println("==implicitCrossJoin")
		val implicitCrossJoin = for {
			c <- Coffees
			s <- Suppliers
		} yield (c.name, s.name)
		implicitCrossJoin foreach println

		println("==implicitInnerJoin")
		val implicitInnerJoin = for {
			c <- Coffees
			s <- Suppliers if c.supID === s.id
		} yield (c.name, s.name)
		implicitInnerJoin foreach println

		println("==explicitCrossJoin")
		val explicitCrossJoin = for {
			(c, s) <- Coffees innerJoin Suppliers
		} yield (c.name, s.name)
		explicitCrossJoin foreach println

		println("==explicitInnerJoin")
		val explicitInnerJoin = for {
			(c, s) <- Coffees innerJoin Suppliers on (_.supID === _.id)
		} yield (c.name, s.name)
		explicitInnerJoin foreach println

		println("==explicitLeftOuterJoin")
		val explicitLeftOuterJoin = for {
			(c, s) <- Coffees leftJoin Suppliers on (_.supID === _.id)
		} yield (c.name, s.name.?)
		explicitLeftOuterJoin foreach println

		println("==explicitRightOuterJoin")
		val explicitRightOuterJoin = for {
			(c, s) <- Coffees rightJoin Suppliers on (_.supID === _.id)
		} yield (c.name.?, s.name)
		explicitRightOuterJoin foreach println

		println("==explicitFullOuterJoin - not support")
		// h2目前好像不支持full outer join，见：
		// http://www.h2database.com/html/roadmap.html - Full outer joins

		//		val explicitFullOuterJoin = for {
		//			(c, s) <- Coffees outerJoin Suppliers on (_.supID === _.id)
		//		} yield (c.name.?, s.name.?)
		//		explicitFullOuterJoin foreach println

		println("==zipJoinQuery")
		val zipJoinQuery = for {
			(c, s) <- Coffees zip Suppliers
		} yield (c.name, s.name)
		zipJoinQuery foreach println

		println("==zipWithJoin")
		val zipWithJoin = for {
			res <- Coffees.zipWith(Suppliers, (c: Coffees.type, s: Suppliers.type) => (c.name, s.name))
		} yield res
		zipWithJoin foreach println

		println("==zipWithIndexJoin")
		val zipWithIndexJoin = for {
			(c, idx) <- Coffees.zipWithIndex
		} yield (c.name, idx)
		zipWithIndexJoin foreach println

		println()
		println("==Unions")
		val uq1 = Query(Coffees).filter(_.price < 8.0)
		val uq2 = Query(Coffees).filter(_.price > 9.0)
		val unionQuery = uq1 union uq2
		val unionAllQuery = uq1 unionAll uq2
		uq1 foreach println
		uq2 foreach println
		unionQuery foreach println
		unionAllQuery foreach println

		{
			println()
			println("==Aggregation")
			val q = Coffees.map(_.price)
			q foreach println

			val q1 = q.min
			val q2 = q.max
			val q3 = q.sum
			val q4 = q.avg

			val qq = Query(Coffees)
			val qq1 = qq.length
			val qq2 = qq.exists

			val gq = (for {
				c <- Coffees
				s <- c.supplier
			} yield (c, s)).groupBy(_._1.supID)
			val gq2 = gq.map {
				case (supID, css) =>
					(supID, css.length, css.map(_._1.price).avg)
			}
			val invoker = gq2.invoker
			println(invoker)
			gq2 foreach println
		}
		{
			println()
			println("==Deleting")
			val q = Query(Coffees).filter {
				e => e.price > 9.0
			}
			val d = q.delete
			println(d)
		}
		{
			println()
			println("==Inserting")
			Coffees.insert("Colombian1", 101, 7.99, 0, 0)
			Coffees.insertAll(
				("Frech_Roast1", 49, 8.99, 0, 0),
				("Espresso1", 150, 9.99, 0, 0)
			)
			(Coffees.name ~ Coffees.supID ~ Coffees.price).insert("Colombian_Decaf1", 101, 8.99)
			val statement = Coffees.insertStatement
			println(statement)
			val invoker = Coffees.insertInvoker
			println(invoker)
		}

		ddl.drop
	}

}
