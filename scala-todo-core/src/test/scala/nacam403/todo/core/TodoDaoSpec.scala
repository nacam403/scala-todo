package nacam403.todo.core

import org.scalatest.{AsyncFunSpec, BeforeAndAfterAll, Matchers}

class TodoDaoSpec extends AsyncFunSpec with BeforeAndAfterAll with Matchers {

  override def beforeAll() = DatabaseManager.migrate()

  override def afterAll() = DatabaseManager.close()

  private val dao = TodoDaoImpl

  describe("list()") {
    it("should return seq") {
      for {
        seqBefore <- dao.list
        _ <- dao.create("Todo 1")
        _ <- dao.create("Todo 2")
        seq <- dao.list
      } yield (seq.length - seqBefore.length) shouldBe 2
    }
  }

  describe("create()") {
    it("should return auto increment ID") {
      for {
        id1 <- dao.create("Todo 1")
        id2 <- dao.create("Todo 2")
      } yield (id2 - id1) shouldBe 1
    }
  }

  describe("get()") {
    describe("with existence ID") {
      it("should return Some[Todo]") {
        for {
          id <- dao.create("Todo")
          todoOption <- dao.get(id)
        } yield todoOption shouldBe Some(new Todo(id, "Todo", false))
      }
    }

    describe("with non existence ID") {
      it("should return None") {
        dao.get(-1).map(_ shouldBe 'isEmpty)
      }
    }
  }

  describe("update()") {
    describe("with existence ID") {
      it("should update todo") {
        for {
          id <- dao.create("Todo")
          updatedRowCount <- dao.update(new Todo(id, "New Todo", true))
          newTodoOption <- dao.get(id)
        } yield {
          updatedRowCount shouldBe 1
          newTodoOption shouldBe Some(new Todo(id, "New Todo", true))
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        dao.update(Todo(Some(-1), "Todo")).map(_ shouldBe 0)
      }
    }
  }

  describe("delete()") {
    describe("with existence ID") {
      it("should delete todo") {
        for {
          id <- dao.create("Todo")
          deletedRowCount <- dao.delete(id)
          todoOption <- dao.get(id)
        } yield {
          deletedRowCount shouldBe 1
          todoOption shouldBe None
        }
      }
    }

    describe("with non existence ID") {
      it("should return 0") {
        dao.delete(-1).map(_ shouldBe 0)
      }
    }
  }

}
