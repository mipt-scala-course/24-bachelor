scalaVersion := "3.4.2"

val catsVersion                 = "2.12.0"
val catsEffectVersion           = "3.5.4"
val redis4catsVersion           = "1.7.0"
val circeVersion                = "0.14.10"
val http4sVersion               = "0.23.16"

libraryDependencies ++= List(
  "org.typelevel"  %% "cats-core"                       % catsVersion,
  "org.typelevel"  %% "cats-effect"                     % catsEffectVersion,
  "dev.profunktor" %% "redis4cats-effects"              % redis4catsVersion,
  "dev.profunktor" %% "redis4cats-log4cats"             % redis4catsVersion,
  "io.circe"       %% "circe-core"                      % circeVersion,
  "io.circe"       %% "circe-generic"                   % circeVersion,
  "io.circe"       %% "circe-parser"                    % circeVersion,
  "org.http4s"     %% "http4s-blaze-client"             % http4sVersion
)
