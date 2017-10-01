package nacam403.todo.core

import slick.jdbc.H2Profile.api._

object TodoDaoImpl extends TodoDao {

  private val db = DatabaseManager.db

  val query = TableQuery[TodoTable]

  override def list = db.run(query.result)

  override def get(id: Long) = db.run(query.filter(_.id === id).result.headOption)

  override def create(description: String) = db.run(
    (query returning query.map(_.id)) += Todo(None, description)
  )

  override def update(todo: Todo) = {
    db.run(
      query.filter(_.id === todo.id)
        .map(current => (current.description, current.done))
        .update((todo.description, todo.done))
    )
  }

  override def delete(id: Long) = db.run(query.filter(_.id === id).delete)

}
