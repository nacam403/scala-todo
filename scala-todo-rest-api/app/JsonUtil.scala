package utils

import nacam403.todo.core.Todo
import play.api.libs.functional.syntax._
import play.api.libs.json._

object JsonUtil {

  implicit val todoWrites = new Writes[Todo] {
    def writes(todo: Todo) = Json.obj(
      "id" -> todo.id,
      "description" -> todo.description,
      "done" -> todo.done
    )
  }

  implicit val todoReads: Reads[Todo] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "description").read[String] and
      (JsPath \ "done").readWithDefault[Boolean](false)
    ) (Todo.apply _)

}
