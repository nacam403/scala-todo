package services

import javax.inject.{Inject, Singleton}

import nacam403.todo.core.DatabaseManager
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

@Singleton
class DatabaseLifecycle @Inject()(appLifecycle: ApplicationLifecycle) {

  DatabaseManager.migrate()

  appLifecycle.addStopHook {() =>
    DatabaseManager.close()
    Future.successful(())
  }

}
