credentials += Credentials("Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("SONATYPE_USER", default = "paradoxicalio"),
  sys.env.getOrElse("SONATYPE_PASSWORD", default = ""))

// Your profile name of the sonatype account. The default is the same with the organization value
publishMavenStyle := true

publishArtifact in Test := false

sonatypeProfileName := "io.paradoxical"

pgpPublicRing := new File(".deployment/gpg/paradoxical-io.pubgpg")
pgpSecretRing := new File(".deployment/gpg/paradoxical-io-private.gpg")
pgpPassphrase := Some(sys.env.getOrElse("GPG_PASSWORD", default = "").toArray)

pomIncludeRepository := { _ => false }

// To sync with Maven central, you need to supply the following information:
pomExtra := (
  <url>https://github.com/paradoxical-io/tasks</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/paradoxical-io/tasks</connection>
      <developerConnection>scm:git:git@github.com:paradoxical-io/tasks.git</developerConnection>
      <url>github.com:paradoxical-io/tasks.git</url>
    </scm>
    <developers>
      <developer>
        <id>devshorts</id>
        <name>Anton Kropp</name>
      </developer>
    </developers>
  )
