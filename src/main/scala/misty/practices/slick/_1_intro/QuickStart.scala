package misty.practices.slick._1_intro

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-11
 * Time: 下午1:46
 */


object QuickStart extends App {

	object Coffees extends Table[(String, Double)]("COFFEES") {
		def name = column[String]("COF_NAME", O.PrimaryKey)

		def price = column[Double]("PRICE")

		def * = name ~ price
	}

	Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
		Coffees.ddl.create
		val names = Coffees.filter(_.price < 10.0).map(_.name).list
		println(names)
	}
}