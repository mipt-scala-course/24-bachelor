ThisBuild / version := "0.1.9"
ThisBuild / scalaVersion := "2.13.12"

ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

lazy val ZioVersion     = "2.0.21"
lazy val ZioConfVersion = "4.0.1"
lazy val ZioLogVersion  = "2.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "homework-s01-09",
    libraryDependencies += "dev.zio"   %% "zio"                 % ZioVersion,
    libraryDependencies += "dev.zio"   %% "zio-concurrent"      % ZioVersion,
    libraryDependencies += "dev.zio"   %% "zio-config"          % ZioConfVersion,
    libraryDependencies += "dev.zio"   %% "zio-config-typesafe" % ZioConfVersion,
    libraryDependencies += "dev.zio"   %% "zio-logging"         % ZioLogVersion,
    libraryDependencies += "dev.zio"   %% "zio-logging-slf4j"   % ZioLogVersion,
    libraryDependencies += "org.slf4j"  % "slf4j-reload4j"      % "1.7.36",
    libraryDependencies += "dev.zio"   %% "zio-test"            % ZioVersion % Test,
  )

addCommandAlias("fmtCheck", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("fmtAll", "all scalafmtSbt scalafmtAll")
