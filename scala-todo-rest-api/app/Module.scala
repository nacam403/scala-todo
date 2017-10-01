import com.google.inject.AbstractModule
import nacam403.todo.core.{TodoDao, TodoTable}
import services.DatabaseService

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[TodoDao]).toInstance(TodoTable)
    bind(classOf[DatabaseService]).asEagerSingleton()
  }

}
