
ThisBuild / scalaVersion := "3.3.3"

ThisBuild / organization := "mipt"
ThisBuild/ version       := "1.0.0-SNAPSHOT"

lazy val root = (project in file("."))
    .settings(
	    name := "test-for-plugin"
    )

