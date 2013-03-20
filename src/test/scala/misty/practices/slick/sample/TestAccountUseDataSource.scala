package misty.practices.slick.sample

import org.slf4j.LoggerFactory
import java.util.concurrent.ArrayBlockingQueue
import concurrent.Future
import org.apache.commons.lang3.RandomStringUtils
import annotation.tailrec
import compat.Platform._
import slick.session.Database
import slick.lifted.Query
import concurrent.duration.DurationLong
import scala.slick.driver.H2Driver.simple._
import scala.concurrent.ExecutionContext.Implicits.global
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-20
 * Time: 下午3:28
 */
object TestAccountUseDataSource extends App {
	val logger = LoggerFactory.getLogger(classOf[TestAccount])

	val ds = DataSource()

	val theEnd = Account(email = "", pwd = "")
	val queue = new ArrayBlockingQueue[Account](100)
	Future {
		1 to 100000 foreach {
			i => queue.put(Account(email = RandomStringUtils.random(10), pwd = RandomStringUtils.random(10)))
		}
		queue.put(theEnd)
	}
	//	val db = Database.forURL("jdbc:h2:testdb", "admin", "admin", driver = "org.h2.Driver")
	val db = Database.forDataSource(ds)

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

		val start = currentTime.millis
		insert(queue.take)
		val end = currentTime.millis
		println(end - start)
		db.withSession {
			Query(Accounts.map(_.id).max) foreach println
		}
		readLine("Press Enter to terminate!")
	} finally {
		db.withSession {
			Accounts.ddl.drop
		}
		ds.close()
	}
}
