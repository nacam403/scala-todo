package controllers

import nacam403.todo.core.{Todo, TodoDao}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import utils.JsonUtil._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TodoControllerSpec extends FunSpec with MockitoSugar {

  val mockDao = mock[TodoDao]

  val controller = new TodoController(Helpers.stubControllerComponents(), mockDao)

  describe("TodoController") {
    it("should create new todo") {
      when(mockDao.create("new todo")).thenReturn(Future(1L))

      val request = FakeRequest().withJsonBody(Json.parse("""{"description":"new todo"}"""))
      val resultFuture = controller.create(request)
      assert(status(resultFuture) == OK)
      val json = contentAsJson(resultFuture)
      assert((json \ "id").as[Long] == 1)
    }

    it("should BadRequest response with illegal create request") {
      val resultFuture = controller.create(FakeRequest())
      assert(status(resultFuture) == BAD_REQUEST)

      val illegalJsonRequest = FakeRequest().withJsonBody(Json.parse("{}"))
      val resultFutureWithIllegalJson = controller.create(illegalJsonRequest)
      assert(status(resultFutureWithIllegalJson) == BAD_REQUEST)
    }

    it("should return todo list") {
      when(mockDao.list).thenReturn(Future {
        Seq(
          new Todo(1, "Todo 1"),
          new Todo(2, "Todo 2")
        )
      })

      val resultFuture = controller.list(FakeRequest())
      assert(status(resultFuture) == OK)
      val json = contentAsJson(resultFuture)
      assert(json.as[Seq[Todo]].length == 2)
    }

    it("should return existence todo") {
      when(mockDao.get(1)).thenReturn(Future {
        Some(new Todo(1, "Todo 1", true))
      })

      val resultFuture = controller.get(1)(FakeRequest())
      assert(status(resultFuture) == OK)
    }

    it("should return NotFound with non existence todo ID for get") {
      when(mockDao.get(-1)).thenReturn(Future(None))

      val resultFuture = controller.get(-1)(FakeRequest())
      assert(status(resultFuture) == NOT_FOUND)
    }

    it("should update existence todo") {
      when(mockDao.update(new Todo(1, "updated todo", true))).thenReturn(Future(1))

      val jsonRequest = FakeRequest().withJsonBody(Json.parse("""{"description":"updated todo", "done":true}"""))
      val resultFuture = controller.update(1)(jsonRequest)
      assert(status(resultFuture) == OK)
    }

    it("should return NotFound with non existence todo ID for update") {
      when(mockDao.update(ArgumentMatchers.any())).thenReturn(Future(0))
      val jsonRequest = FakeRequest().withJsonBody(Json.parse("""{"description":"updated todo", "done":true}"""))
      val resultFuture = controller.update(-1)(jsonRequest)
      assert(status(resultFuture) == NOT_FOUND)
    }

    it("should delete existence todo") {
      when(mockDao.delete(1)).thenReturn(Future(1))

      val resultFuture = controller.delete(1)(FakeRequest())
      assert(status(resultFuture) == OK)
    }

    it("should return NotFound with non existence todo ID for delete") {
      when(mockDao.delete(-1)).thenReturn(Future(0))

      val resultFuture = controller.delete(-1)(FakeRequest())
      assert(status(resultFuture) == NOT_FOUND)
    }

  }

}
