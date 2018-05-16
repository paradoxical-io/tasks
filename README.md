Tasks
===
[![Build Status](https://travis-ci.org/paradoxical-io/tasks.svg?branch=master)](https://travis-ci.org/paradoxical-io/tasks)
[![Maven Central](https://img.shields.io/maven-central/v/io.paradoxical/tasks_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22tasks_2.12t%22)

Unified way to create a single main entrypoint that supports
argument parsing and multiple commands.  Create tasks 
that can be delegated to 

## Examples

Tasks are easy to make, they serve as individual "mains" that can be delegated to:

As an example from [Carlyle](https://github.com/paradoxical-io/carlyle)

```scala
object App extends TaskEnabledApp {
  DateTimeZone.setDefault(DateTimeZone.UTC)

  override def appName: String = "carlyle"

  override def tasks: List[Task] = List(new ServerTask(), new MigrateDbTask)
}
```

Where each task looks like:

```scala
case class ServerConfig(
  metricsEnabled: Boolean = true
)

class ServerTask()(implicit executionContext: ExecutionContext) extends Task {
  override type Config = ServerConfig

  override def emptyConfig: ServerConfig = ServerConfig()

  override def definition: TaskDefinition[ServerConfig] = {
    new TaskDefinition[ServerConfig](
      name = "server",
      description = "Server",
      args =
        _.opt[Unit]("disableMetrics").
          action((_, config) => {
            config.copy(metricsEnabled = false)
          })
    )
  }

  override def execute(args: ServerConfig): Unit = {
    new Server(Modules(args.metricsEnabled)).main(Array.empty)
  }
}
```

```scala
class MigrateDbTask extends Task {
  override type Config = Unit

  override def emptyConfig: Unit = Unit

  override def definition: TaskDefinition[Unit] = {
    TaskDefinition(
      name = "migrate-db",
      description = "Migrates the carlyle db or creates a new one"
    )
  }

  override def execute(args: Unit): Unit = {
    val migrator = Modules().injector().make[Migrator]

    migrator.migrate()
  }
}
```

Here we have an app that defines 2 tasks. If we run this app with no arguments we'll get:

```
Error: Command required
Usage: carlyle [server|migrate-db] [options]

Command: server
Server
Command: migrate-db
Migrates the carlyle db or creates a new one
  --help              prints this usage text

Select a command to run. Each command may have subcommands.
```

If we run a command any unknown command (`-h` is fine):

```
$ docker run -it paradoxical/carlyle server -h
Error: Unknown option -h
Usage: server [options]

  --disableMetrics
```

You can now see that the `server` task is configured to accept an optional `--disableMetrics` command line argument.

In this way each task can define its own arguments and the main acts as a dispatcher given the root command.  


