
ThisBuild / organization := "tinkoff.mipt"
ThisBuild / version      := "1.12.0"


lazy val root = (project in file("."))
    .enablePlugins(SbtPlugin)
    .settings(
	    name := "counting-plugin",
    )
