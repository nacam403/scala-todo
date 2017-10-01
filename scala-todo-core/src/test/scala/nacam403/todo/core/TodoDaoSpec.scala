package nacam403.todo.core

import org.scalatest.{AsyncFunSpec, BeforeAndAfterAll, Matchers}

class TodoDaoSpec extends AsyncFunSpec with BeforeAndAfterAll with Matchers {

  override def beforeAll() = DatabaseManager.migrate()

  override def afterAll() = DatabaseManager.close()

  describe("list()") {
    it("should return seq") {
      for {
        seqBefore <- TodoTable.list
        _ <- TodoTable.create("Todo 1")
        _ <- TodoTable.create("Todo 2")
        seq <- TodoTable.list
      } yield (seq.length - seqBefore.length) shouldBe 2
    }
  }

  describe("create()") {
    it("should return auto increment ID") {
      for {
        id1 <- TodoTable.create("Todo 1")
        id2 <- TodoTable.create("Todo 2")
      } yield (id2 - id1) shouldBe 1
    }
  }

  describe("get()") {
    describe("with existence ID") {
      it("should return Some[Todo]") {
        for {
          id <- TodoTable.create("Todo")
          todoOption <- TodoTable.get(id)
        } yield todoOption shouldBe Some(new Todo(id, "Todo", false))
      }
    }

    describe("with non existence ID") {
      it("should return None") {
        TodoTable.get(-1).map(_ shouldBe 'isEmpty)
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
          updatedRowCount shouldBe 1
          newTodoOption shouldBe Some(new Todo(id, "New Todo", true))
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        TodoTable.update(Todo(Some(-1), "Todo")).map(_ shouldBe 0)
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
          deletedRowCount shouldBe 1
          todoOption shouldBe None
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        TodoTable.delete(-1).map(_ shouldBe 0)
      }
    }
  }

}
