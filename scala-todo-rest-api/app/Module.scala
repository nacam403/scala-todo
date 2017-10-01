import com.google.inject.AbstractModule
import nacam403.todo.core.{TodoDao, TodoDaoImpl}
import services.DatabaseLifecycle

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[TodoDao]).toInstance(TodoDaoImpl)
    bind(classOf[DatabaseLifecycle]).asEagerSingleton()
  }

}
