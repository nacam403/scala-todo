package nacam403.todo.core

case class Todo(id: Option[Long] = None, description: String, done: Boolean = false) {

  def this(id: Long, description: String, done: Boolean) = {
    this(Some(id), description, done)
  }

  def this(id: Long, description: String) = this(id, description, false)

}
