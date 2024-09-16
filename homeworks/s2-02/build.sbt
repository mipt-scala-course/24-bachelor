name := "bachelor-homeworks-s2-02"


scalaVersion := "3.3.3"

scalacOptions := List(
  "-encoding",
  "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:_",
)
libraryDependencies += "org.scalameta"      %% "munit"        % "1.0.1"     % "test"