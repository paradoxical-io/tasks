import sbt._
import BuildConfig.Dependencies

lazy val commonSettings = BuildConfig.commonSettings()

commonSettings

name := "tasks"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.7.0"
) ++ Dependencies.testDeps

lazy val showVersion = taskKey[Unit]("Show version")

showVersion := {
  println(version.value)
}

// custom alias to hook in any other custom commands
addCommandAlias("build", "; compile")
