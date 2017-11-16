package io.paradoxical.tasks


/**
 * An instance of a task that can be run
 */
trait Task  {
  type Config

  def emptyConfig: Config

  def definition: TaskDefinition[Config]

  /**
   * Override to provide a custom message (left) if the
   * config is invalid. Otherwise return Right
   *
   * @param config
   * @return
   */
  def isValid(config: Config): Either[String, Unit] = Right(Unit)

  def execute(args: Config): Unit
}

/**
 * Mix into an object to get a main method
 * that provides argument parsing and task delegation
 */
trait TaskEnabledApp {

  case class AppConfig(cmd: String = "")

  def appName: String

  def tasks: List[Task]

  def defaultTask: Option[Task] = None

  def exitOnComplete: Boolean = true

  def main(args: Array[String]): Unit = {

    var exitCode = 1
    try {
      runTask(args)
      exitCode = 0
    } catch {
      case e: Exception =>
        e.printStackTrace()
        throw e
    } finally {
      // Note: we may have a non-zero exit code here but the exception not logged.
      taskExit(exitCode)
    }
  }

  protected def execute(task: Task)(commandConfig: task.Config): Unit = {
    task.execute(commandConfig)
  }

  protected def runTask(args: Array[String]): Unit = {
    val mainCommandParser = new scopt.OptionParser[AppConfig](appName) {
      override def showUsageOnError = true

      tasks.foreach(task => {
        cmd(task.definition.name).
          action((_, value) => value.copy(cmd = task.definition.name)).
          text(task.definition.description)
      })

      help("help").text("prints this usage text")

      checkConfig(app => {
        if (app.cmd.trim.isEmpty && defaultTask.isEmpty) {
          Left("Command required")
        } else {
          Right(Unit)
        }
      })

      note(System.lineSeparator() + "Select a command to run. Each command may have subcommands.  " +
           defaultTask.map(task =>
             s"""If no arguments are provided the '${task.definition.name}'
                |command will be executed with the default params: ${task.emptyConfig}""".stripMargin).
             getOrElse("")
      )
    }

    // parse the first command
    mainCommandParser.parse(args.take(1), AppConfig()) match {
      case Some(config) if config != AppConfig() =>
        // if we have a valid command, parse its sub arguments
        val commandArgs = args.drop(1)

        val task = tasks.find(_.definition.name == config.cmd).get

        val commandParser = new scopt.OptionParser[task.Config](task.definition.name) {
          override def showUsageOnError = true

          task.definition.args.foreach(arg => arg(this))

          checkConfig(task.isValid)
        }

        commandParser.parse(commandArgs, task.emptyConfig) match {
          case Some(commandConfig) =>
            execute(task)(commandConfig)
          case _ =>
        }

      case Some(config) if defaultTask.isDefined =>
        defaultTask.foreach(task => execute(task)(task.emptyConfig))
      case _ =>
    }
  }

  protected def taskExit(code: Int): Unit = {
    if (exitOnComplete) {
      System.exit(code)
    }
  }
}
