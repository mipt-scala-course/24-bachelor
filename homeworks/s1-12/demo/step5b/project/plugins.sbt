
// local repositories
resolvers += Resolver.mavenLocal
resolvers += MavenCache("custom-local-maven", file("path/to/maven-repo/releases"))
resolvers += Resolver.file("my-test-local-repo", file("test"))
resolvers += Resolver.url("my-test-remote-repo", url("https://example.org/repo-releases/"))

// Sonatype repositories
resolvers ++= Resolver.sonatypeOssRepos("public")
resolvers ++= Resolver.sonatypeOssRepos("staging")
resolvers ++= Resolver.sonatypeOssRepos("snapshots")
resolvers ++= Resolver.sonatypeOssRepos("releases")

// Typesafe repositories
resolvers += Resolver.typesafeRepo("public")
resolvers += Resolver.typesafeRepo("snapshots")
resolvers += Resolver.typesafeRepo("releases")

// Ivy repositories
resolvers += Resolver.typesafeIvyRepo("public")
resolvers += Resolver.typesafeIvyRepo("snapshots")
resolvers += Resolver.typesafeIvyRepo("releases")

// Community repository at scala-sbt.org
resolvers += Resolver.sbtPluginRepo("snapshots")
resolvers += Resolver.sbtPluginRepo("releases")

// Bintray repositories
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("owner", "repo") // e.g. https://dl.bintray.com/[owner]/[[repo]/

// java.net Maven2 repository
resolvers += JavaNet2Repository

// Custom repositories
resolvers += "May own cool repo" at "https://my.cool.host.com/repo/"



addDependencyTreePlugin
