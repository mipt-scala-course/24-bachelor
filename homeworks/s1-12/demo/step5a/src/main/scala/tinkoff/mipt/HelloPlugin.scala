package tinkoff.mipt

import sbt._
import Keys._
import complete.DefaultParsers._

object HelloPlugin extends AutoPlugin {
    
    override def trigger = allRequirements
    override def requires = empty

    object autoImport {
        val countSourceFiles= taskKey[Unit]("Count source files")
        val countSourceLines= taskKey[Unit]("Count source lines")
    }
    import autoImport._


    override def projectSettings: Seq[Def.Setting[_]] =
        inConfig(Compile)(countingSettings)

    lazy val countingSettings: Seq[Def.Setting[_]] =
        Seq(
            countSourceFiles := {
                System.out.println(s"Counting source files...")
                System.out.println(s"${sources.value.size} files found")
                System.out.println(s"Counting source files... done")
            },
            countSourceLines := {
                System.out.println(s"Counting source lines...")
                for {
                    file  <- sources.value
                    lines  = IO.readLines(file)
                } yield System.out.println(s"${file.name} contains ${lines.size} lines")
                System.out.println(s"Counting source lines... done")
            }
        )

}