package nacam403.todo.core

import org.scalatest.{AsyncFunSpec, BeforeAndAfterAll}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TodoDaoSpec extends AsyncFunSpec with BeforeAndAfterAll {

  val db = TodoConfig.db

  override def beforeAll() = Await.result(db.run(TodoTable.schema.create), Duration.Inf)

  override def afterAll() = db.close()

  describe("list()") {
    describe("with empty table") {
      it("should return empty seq") {
        TodoTable.list.map(seq => assert(seq.isEmpty))
      }
    }

    describe("with non empty table") {
      it("should return non empty seq") {
        for {
          _ <- TodoTable.create("Todo 1")
          _ <- TodoTable.create("Todo 2")
          seq <- TodoTable.list
        } yield assert(seq.length == 2)
      }
    }
  }

  describe("create()") {
    it("should return auto increment ID") {
      for {
        id1 <- TodoTable.create("Todo 1")
        id2 <- TodoTable.create("Todo 2")
      } yield assert(id2 - id1 == 1)
    }
  }

  describe("get()") {
    describe("with existence ID") {
      it("should return Some[Todo]") {
        for {
          id <- TodoTable.create("Todo")
          todoOption <- TodoTable.get(id)
        } yield assert(todoOption.get == new Todo(id, "Todo", false))
      }
    }

    describe("with non existence ID") {
      it("should return None") {
        TodoTable.get(-1).map(todoOption => assert(todoOption.isEmpty))
      }
    }
  }

  describe("update()") {
    describe("with existence ID") {
      it("should update todo") {
        for {
          id <- TodoTable.create("Todo")
          updatedRowCount <- TodoTable.update(new Todo(id, "New Todo", true))
          newTodoOption <- TodoTable.get(id)
        } yield {
          assert(updatedRowCount == 1)
          assert(newTodoOption.get == new Todo(id, "New Todo", true))
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        TodoTable.update(Todo(Some(-1), "Todo")).map(updatedRowCount => assert(updatedRowCount == 0))
      }
    }
  }

  describe("delete()") {
    describe("with existence ID") {
      it("should delete todo") {
        for {
          id <- TodoTable.create("Todo")
          deletedRowCount <- TodoTable.delete(id)
          todoOption <- TodoTable.get(id)
        } yield {
          assert(deletedRowCount == 1)
          assert(todoOption.isEmpty)
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        TodoTable.delete(-1).map(deletedRowCount => assert(deletedRowCount == 0))
      }
    }
  }

}
