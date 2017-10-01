package utils

import nacam403.todo.core.Todo
import org.scalatest.FunSpec
import play.api.libs.json.Json
import utils.JsonUtil._

class JsonUtilSpec extends FunSpec {

  describe("JsonUtil") {
    it("should convert todo to JSON") {
      val json = Json.toJson(new Todo(1, "Todo", true))
      assert((json \ "id").as[Long] == 1)
      assert((json \ "description").as[String] == "Todo")
      assert((json \ "done").as[Boolean] == true)
    }

    it("should convert JSON to todo") {
      val todo = new Todo(1, "Todo", true)
      val json = Json.toJson(todo)
      assert(todo == json.as[Todo])
    }

    it("should convert JSON with only description to todo") {
      val todo = new Todo(None, "Todo")
      val json = Json.toJson(todo)
      assert(todo == json.as[Todo])
    }
  }

}
