ThisBuild / organization := "ru.tinkoff.mipt"
version := "1.0.0"

scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "demo"
  )
