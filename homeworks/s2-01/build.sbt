name := "bachelor-homeworks-s2-01"


scalaVersion := "3.5.0"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
)
libraryDependencies += "org.scalameta"      %% "munit"        % "1.0.1"     % "test"