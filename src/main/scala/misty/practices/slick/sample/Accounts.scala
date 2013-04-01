package misty.practices.slick.sample

import scalafx.beans.property.{ReadOnlyIntegerProperty, StringProperty}
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import org.apache.commons.lang3.RandomStringUtils
import java.util.concurrent.ArrayBlockingQueue
import concurrent.Future
import org.slf4j.LoggerFactory
import compat.Platform
import concurrent.duration.DurationLong
import scala.concurrent.ExecutionContext.Implicits.global
import annotation.tailrec

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-20
 * Time: 下午12:04
 */
object Accounts extends Table[Account]("ACCOUNTS") {
	def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

	def email = column[String]("EMAIL")

	def pwd = column[String]("PASSWORD")

	def * = id ~ email ~ pwd <>(Account.apply _, Account.unapply _)
}

class Account(_id: Option[Int] = None,
			  _email: String = null,
			  _pwd: String = null) {
	lazy val id = new ReadOnlyIntegerProperty(this, "id", _id.get)

	lazy val email = new StringProperty(this, "email", _email)

	lazy val password = new StringProperty(this, "password", _pwd)

	def email_=(v: String) {
		email() = v
	}

	def password_=(v: String) {
		password() = v
	}
}

object Account {
	def apply(id: Option[Int] = None, email: String, pwd: String) = new Account(id, email, pwd)

	def unapply(acc: Account) = Option(None, acc.email(), acc.password())
}

class TestAccount

object TestAccount extends App {
	val logger = LoggerFactory.getLogger(classOf[TestAccount])
	val theEnd = Account(email = "", pwd = "")
	val queue = new ArrayBlockingQueue[Account](100)
	Future {
		1 to 100 foreach {
			i => queue.put(Account(email = RandomStringUtils.random(10), pwd = RandomStringUtils.random(10)))
		}
		queue.put(theEnd)
	}
	val db = Database.forURL("jdbc:h2:testdb", "admin", "admin", driver = "org.h2.Driver")

	try {
		db.withSession {
			Accounts.ddl.create
			Accounts.insert(Account(email = RandomStringUtils.random(10), pwd = RandomStringUtils.random(10)))
			logger.debug("The test instance has inserted!")
		}

		@tailrec def insert(acc: Account) {
			if (acc != theEnd) {
				db.withSession {
					Accounts.insert(acc)
				}
				insert(queue.take)
			}
		}

		readLine("Press Enter to continue")

		val start = Platform.currentTime.millis
		insert(queue.poll())
		val end = Platform.currentTime.millis
		println(end - start)
		db.withSession {
			Query(Accounts.map(_.id).max) foreach println
		}
	} finally {
		db.withSession {
			Accounts.ddl.drop
		}
	}
	readLine("Press Enter to terminate!")
}