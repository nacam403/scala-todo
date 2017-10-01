package nacam403.todo.core

import scala.concurrent.Future

trait TodoDao {

  def list: Future[Seq[Todo]]

  def get(id: Long): Future[Option[Todo]]

  def create(description: String): Future[Long]

  def update(todo: Todo): Future[Int]

  def delete(id: Long): Future[Int]

}
