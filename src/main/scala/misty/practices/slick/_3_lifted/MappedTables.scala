package misty.practices.slick._3_lifted

import scala.slick.driver.H2Driver.simple._
import slick.session.Database
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-11
 * Time: 下午6:24
 */
object MappedTables extends App {

	case class User(id: Option[Int], first: String, last: String)

	object Users extends Table[User]("users") {
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

		def first = column[String]("first")

		def last = column[String]("last")

		def * = id.? ~ first ~ last <>(User.apply _, User.unapply _)
	}

	object A extends Table[(Int, Int)]("a") {
		def k1 = column[Int]("k1")

		def k2 = column[Int]("k2")

		def * = k1 ~ k2

		def idx = index("idx_a", (k1, k2), unique = true)
	}

	val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
	val ddl = Users.ddl ++ A.ddl
	db withSession {
		ddl.create
		//...
		ddl.drop
	}

	ddl.createStatements.foreach(println)
	ddl.dropStatements.foreach(println)
}
