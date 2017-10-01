package utils

import nacam403.todo.core.Todo
import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json.Json
import utils.JsonUtil._

class JsonUtilSpec extends FunSpec with Matchers {

  describe("JsonUtil") {
    it("should convert todo to JSON") {
      val json = Json.toJson(new Todo(1, "Todo", true))
      (json \ "id").as[Long] shouldBe 1
      (json \ "description").as[String] shouldBe "Todo"
      (json \ "done").as[Boolean] shouldBe true
    }

    it("should convert JSON to todo") {
      val todo = new Todo(1, "Todo", true)
      val json = Json.toJson(todo)
      json.as[Todo] shouldBe todo
    }

    it("should convert JSON with only description to todo") {
      val todo = new Todo(None, "Todo")
      val json = Json.toJson(todo)
      json.as[Todo] shouldBe todo
    }
  }

}
