package misty.practices.slick._2_started

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-11
 * Time: 下午2:13
 */
object FirstExample extends App {

	// Definition of the SUPPLIERS table
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

	// Definition of the COFFEES table
	object Coffees extends Table[(String, Int, Double, Int, Int)]("COFFEES") {
		def name = column[String]("COF_NAME", O.PrimaryKey)

		def supID = column[Int]("SUP_ID")

		def price = column[Double]("PRICE")

		def sales = column[Int]("SALES")

		def total = column[Int]("TOTAL")

		def * = name ~ supID ~ price ~ sales ~ total

		// A reified foreign key relation that can be navigated to create a join
		def supplier = foreignKey("SUP_FK", supID, Suppliers)(_.id)
	}

	Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
		// Create the tables, including primary and foreign keys
		(Suppliers.ddl ++ Coffees.ddl).create

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

		// Iterate through all coffees and output them
		Query(Coffees) foreach {
			case (name, supID, price, sales, total) =>
				println("  " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
		}

		println("===============")

		// Why not let the database do the string conversion and concatenation?
		val q1 = for (c <- Coffees) // Coffees lifted automatically to a Query
		yield ConstColumn("  ") ++ c.name ++ "\t" ++ c.supID.asColumnOf[String] ++
				"\t" ++ c.price.asColumnOf[String] ++ "\t" ++ c.sales.asColumnOf[String] ++
				"\t" ++ c.total.asColumnOf[String]
		// The first string constant needs to be lifted manually to a ConstColumn
		// so that the proper ++ operator is found
		q1 foreach println

		println("===============")

		// Perform a join to retrieve coffee names and supplier names for
		// all coffees costing less than $9.00
		val q2 = for {
			c <- Coffees if c.price < 9.0
			s <- Suppliers if s.id === c.supID // use =!= instead of !=
		} yield (c.name, s.name)
		q2 foreach println

		println("===============")
	}
}
