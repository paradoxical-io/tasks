package io.paradoxical.tasks

case class ServerConfig(port: Long = 9999)

/**
 * An example task
 */
class OpsTask extends Task {
  override type Config = Unit

  override def emptyConfig: Unit = {}

  override def definition: TaskDefinition[Unit] = {
    new TaskDefinition[Unit](
      name = "ops",
      description = "Runs an ops task",
      args = _.opt[Unit]("run").required(), _.opt[Unit]("foo")
    )
  }

  override def execute(args: Unit): Unit = {}
}

/**
 * Sample task with options and validation
 */
class ServerTask extends Task {
  override type Config = ServerConfig

  override def emptyConfig: ServerConfig = ServerConfig()

  override def definition: TaskDefinition[ServerConfig] = new TaskDefinition[ServerConfig](
    name = "server",
    description = "Runs the server command",
    args =
      _.opt[Long]("port").
        required().
        action((port, config) => config.copy(port = port)).
        text("Server port")
  )

  /**
   * Override to provide a custom message (left) if the
   * config is invalid. Otherwise return Right
   *
   * @param config
   * @return
   */
  override def isValid(config: ServerConfig): Either[String, Unit] = {
    if (config.port < 1000) {
      Left(s"Please use a port above 1000. You used ${config.port}")
    } else {
      Right(Unit)
    }
  }

  var config: ServerConfig = _

  override def execute(args: ServerConfig): Unit = {
    println(args)

    config = args
  }
}

/**
 * Sample main app
 */
class TaskApp extends TaskEnabledApp {
  override def appName: String = "testapp"

  override def tasks: List[Task] = List(
    new ServerTask,
    new OpsTask
  )

  override def exitOnComplete: Boolean = false
}

