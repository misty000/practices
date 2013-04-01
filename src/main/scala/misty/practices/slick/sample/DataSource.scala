package misty.practices.slick.sample

import com.alibaba.druid.pool.DruidDataSource

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-20
 * Time: 下午4:33
 */
object DataSource {
	def apply() = {
		apply("testdb")
	}

	def apply(name: String) = {
		val ds = new DruidDataSource()
		ds.setUrl("jdbc:h2:" + name)
		ds.setUsername("admin")
		ds.setPassword("admin")
		config(ds)
	}

	private def config(ds: DruidDataSource) = {
		ds.setDriverClassName("org.h2.Driver")
		ds.setPoolPreparedStatements(true)
		ds.setRemoveAbandoned(true)
		ds.setFilters("stat")
		ds.setValidationQuery("select 1")
		ds.setDefaultAutoCommit(true)
		ds
	}
}
