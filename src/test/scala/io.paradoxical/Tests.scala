package io.paradoxical.tasks

import org.scalatest._

class Tests extends FlatSpec with Matchers {
  "Tasks" should "register" in {
    new TaskApp().main(Array())
  }

  it should "call the validation function " in {
    var executed: Boolean = false

    new TaskApp() {
      override protected def execute(task: Task)(commandConfig: task.Config): Unit = {
        super.execute(task)(commandConfig)

        executed = true
      }
    }.main(Array("server", "--port", "123"))

    assert(!executed)
  }

  it should "parse" in {
    var executed: Boolean = false

    new TaskApp() {
      override protected def execute(task: Task)(commandConfig: task.Config): Unit = {
        super.execute(task)(commandConfig)

        assert(task.asInstanceOf[ServerTask].config.port == 1230)

        executed = true
      }
    }.main(Array("server", "--port", "1230"))

    assert(executed)
  }

  it should "support default tasks" in {
    var executed: Boolean = false

    new TaskApp() {
      override def defaultTask: Option[Task] = Some(new ServerTask)

      override protected def execute(task: Task)(commandConfig: task.Config): Unit = {
        super.execute(task)(commandConfig)

        assert(task.asInstanceOf[ServerTask].config.port == ServerConfig().port)

        executed = true
      }
    }.main(Array())

    assert(executed)
  }

  ignore should "support default tasks with help" in {
    var executed: Boolean = false

    new TaskApp() {
      override def defaultTask: Option[Task] = Some(new ServerTask)

      override protected def execute(task: Task)(commandConfig: task.Config): Unit = {
        super.execute(task)(commandConfig)

        executed = true
      }
    }.main(Array("--help"))

    assert(!executed)
  }

  it should "support run other task" in {
    var executed: Boolean = false

    new TaskApp() {
      override def defaultTask: Option[Task] = Some(new ServerTask)

      override protected def execute(task: Task)(commandConfig: task.Config): Unit = {
        super.execute(task)(commandConfig)

        assert(task.isInstanceOf[OpsTask])

        executed = true
      }
    }.main(Array("ops", "--run"))

    assert(executed)
  }
}
