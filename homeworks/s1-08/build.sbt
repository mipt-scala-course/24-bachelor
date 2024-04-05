ThisBuild / scalaVersion := "2.13.10"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-Ymacro-annotations"
)

lazy val root = (project in file("."))
  .settings(
    name := "bachelor-s01-08",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
  )
