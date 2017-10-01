package services

import javax.inject.{Inject, Singleton}

import nacam403.todo.core.{TodoConfig, TodoTable}
import play.api.inject.ApplicationLifecycle
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class DatabaseService @Inject() (appLifecycle: ApplicationLifecycle) {

  val db = TodoConfig.db
  // とりあえずインメモリDBを使うので、テーブルを毎度作る。
  Await.result(db.run(TodoTable.schema.create), Duration.Inf)

  appLifecycle.addStopHook {() =>
    TodoConfig.db.close()
    Future.successful(())
  }

}
