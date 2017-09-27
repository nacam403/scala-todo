package nacam403.todo.core

import slick.jdbc.H2Profile.api._

class TodoTable(tag: Tag) extends Table[Todo](tag, "TODOS") {

  private def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  private def description = column[String]("DESCRIPTION")
  private def done = column[Boolean]("DONE", O.Default(false))

  def * = (id.?, description, done) <> (Todo.tupled, Todo.unapply)

}

object TodoTable extends TableQuery(new TodoTable(_)) with TodoDao {

  private val db = TodoConfig.db

  override def list = db.run(this.result)

  override def get(id: Long) = db.run(this.filter(_.id === id).result.headOption)

  override def create(description: String) = db.run(
    (this returning this.map(_.id)) += Todo(None, description)
  )

  override def update(todo: Todo) = {
    db.run(
      this.filter(_.id === todo.id)
        .map(current => (current.description, current.done))
        .update((todo.description, todo.done))
    )
  }

  override def delete(id: Long) = db.run(this.filter(_.id === id).delete)

}
