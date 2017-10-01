package nacam403.todo.core

import com.typesafe.config.ConfigFactory
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DatabaseManager {

  val db = Database.forConfig("h2mem", ConfigFactory.load("slick"))

  def migrate() = {
    Await.result(db.run(TodoTable.schema.create), Duration.Inf)
  }

  def close() = db.close()

}
