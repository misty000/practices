package misty.practices.slick.sample

import scalafx.beans.property.{IntegerProperty, StringProperty}
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-19
 * Time: 下午11:35
 */
object Persons extends Table[Person]("persons") {
	def name = column[String]("name", O.NotNull)

	def age = column[Int]("age")

	def * = name ~ age <>(Persons.apply _, Persons.unapply _)

	def apply(name: String, age: Int) = new Person(name, age)

	def unapply(p: Person) = Option((p.name(), p.age()))
}

class Person(_name: String, _age: Int) {
	def this() = this(null, 0)

	lazy val name = new StringProperty(this, "name", _name)
	lazy val age = new IntegerProperty(this, "age", _age)

	def name_=(v: String) {
		name() = v
	}

	def age_=(v: Int) {
		age() = v
	}

	override def toString = s"name = ${name()}; age = ${age()}"
}

object UsePerson extends App {
	val p = new Person("misty", 23)
	val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
	val ddl = Persons.ddl
	db withSession {
		ddl.create
		Persons.insert(p)
		val q = Query(Persons)
		q foreach println
		ddl.drop
	}
}