package misty.practices.slick.sample

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import org.apache.commons.lang3.RandomStringUtils

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-20
 * Time: 下午4:36
 */
object Test extends App {
	val db = Database.forDataSource(DataSource())
	createTable(db)
	1 to 100000 foreach (i => {
		db withSession {
			Accounts.insert(
				Account(email = RandomStringUtils.random(10), pwd = RandomStringUtils.random(10))
			)
		}
	})
	readLine("Press Enter")

	def dropTable(db: Database) {
		db withSession {
			Accounts.ddl.drop
		}
	}

	def createTable(db: Database) {
		db withSession {
			Accounts.ddl.create
		}
	}
}
