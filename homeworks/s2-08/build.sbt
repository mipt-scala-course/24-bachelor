name := "bachelor-homeworks-s2-08"

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

libraryDependencies += "dev.zio"  %% "zio"        % "2.1.11"
libraryDependencies += "dev.zio"  %% "zio-test"   % "2.1.11" % Test