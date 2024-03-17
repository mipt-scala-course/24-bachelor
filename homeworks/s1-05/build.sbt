import _root_.sbt.Keys._
import wartremover.Wart
import wartremover.Wart._

ThisBuild / version := "0.1.5"
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
    name := "bachelor-s01-05",
    libraryDependencies += "dev.zio"        %% "zio"        % "2.0.21",
    libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.2.0" % "test",
    libraryDependencies += "dev.zio"        %% "zio-test"   % "2.0.21" % "test"
  )

wartremoverErrors ++= Seq[Wart](Any, AsInstanceOf, Null, Return, Throw, While, MutableDataStructures)
