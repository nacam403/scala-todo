package nacam403.todo.core

import slick.jdbc.H2Profile.api._

class TodoTable(tag: Tag) extends Table[Todo](tag, "TODOS") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def description = column[String]("DESCRIPTION")

  def done = column[Boolean]("DONE", O.Default(false))

  def * = (id.?, description, done) <> (Todo.tupled, Todo.unapply)

}

object TodoTable extends TableQuery(new TodoTable(_))
