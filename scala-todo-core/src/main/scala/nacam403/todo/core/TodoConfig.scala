package nacam403.todo.core

import com.typesafe.config.ConfigFactory
import slick.jdbc.H2Profile.api._

object TodoConfig {

  val db = Database.forConfig("h2mem", ConfigFactory.load("slick"))

}
