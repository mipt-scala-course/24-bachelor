name := "bachelor-homeworks-s2-04"

scalaVersion := "3.4.2"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-Ykind-projector"
)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "6.0.0"  % Test
