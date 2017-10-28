package nacam403.todo.core

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import slick.jdbc.H2Profile.api._

object DatabaseManager {

  val config = ConfigFactory.load("slick").getConfig("h2")
  val db = Database.forConfig("", config)

  def migrate() = {
    val url = config.getString("url")
    val user = if (config.hasPath("user")) config.getString("user") else null
    val password = if (config.hasPath("password")) config.getString("password") else null

    val flyway = new Flyway
    flyway.setDataSource(url, user, password)
    flyway.migrate()
  }

  def close() = db.close()

}
