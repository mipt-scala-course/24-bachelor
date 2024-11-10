name := "bachelor-homeworks-s2-09"

scalaVersion := "3.5.1"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-Ykind-projector"
)

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.5"
