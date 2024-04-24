ThisBuild / version := "0.1.9"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "homework-s01-10",
    libraryDependencies += "eu.timepit"    %% "refined"   % "0.11.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0"  % Test,
  )

addCommandAlias("fmtCheck", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("fmtAll", "all scalafmtSbt scalafmtAll")
