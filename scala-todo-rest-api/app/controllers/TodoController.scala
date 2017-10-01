package controllers

import javax.inject.{Inject, Singleton}

import nacam403.todo.core.{Todo, TodoDao}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.JsonUtil._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoController @Inject()(cc: ControllerComponents, dao: TodoDao)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def create = Action.async { request =>
    request.body.asJson match {
      case Some(json) => {
        json.validate[Todo].fold(
          errors => Future(BadRequest),
          todo => {
            dao.create(todo.description).map { todoId =>
              val json = Json.parse(s"""{"id":$todoId}""")
              Ok(json)
            }
          }
        )
      }
      case None => Future(BadRequest)
    }
  }


  def list = Action.async {
    dao.list.map(todoSeq => Ok(Json.toJson(todoSeq)))
  }


  def get(id: Long) = Action.async {
    dao.get(id).map(_ match {
      case Some(todo) => Ok(Json.toJson(todo))
      case None => NotFound
    })
  }

  def update(id: Long) = Action.async { request =>
    request.body.asJson match {
      case Some(json) => {
        json.validate[Todo].fold(
          errors => Future(BadRequest),
          todo => {
            val newTodo = todo.copy(id = Some(id))
            dao.update(newTodo).map(_ match {
              case 1 => Ok
              case _ => NotFound
            })
          }
        )
      }
      case None => Future(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    dao.delete(id).map(_ match {
      case 1 => Ok
      case _ => NotFound
    })
  }

}
