package tinkoff.mipt

import sbt._
import Keys._
import complete.DefaultParsers._

object HelloPlugin extends AutoPlugin {
    
    override def trigger = allRequirements
    override def requires = empty

    object autoImport {
        val helloSetting    = settingKey[String]("Hello prefix")
        val helloTask       = taskKey[Unit]("Say hello")
        val helloInput      = inputKey[Unit]("Say hello with input")

        val countSourceFiles= taskKey[Unit]("Count source files")
    }
    import autoImport._


    override def buildSettings: Seq[Def.Setting[_]] =
        Seq(
            commands += helloCommand,
        )

    override def projectSettings: Seq[Def.Setting[_]] =
        helloSettings ++ inConfig(Compile)(countingSettings)

    override def globalSettings: Seq[Def.Setting[_]] =
        Seq(
            helloSetting := "Hello with",
        )


    lazy val helloCommand =
        Command.command("hello") {
            (state: State) =>
                println("*"*48)
                println("* Hi!")
                println("*"*48)
                state
        }


    lazy val helloSettings: Seq[Def.Setting[_]] =
        Seq(
            helloInput := {
                val args = spaceDelimited("").parsed
                System.out.println(s"""${helloSetting.value} ${args.mkString("<", "> <", ">")}""")
            },
            helloTask := {
                val logger = streams.value.log
                val greets = helloSetting.value + " a big smile"
                logger.info(greets)
            }
        )

    lazy val countingSettings: Seq[Def.Setting[_]] =
        Seq(
            countSourceFiles := {
                System.out.println(s"Counting source files...")
                System.out.println(s"${sources.value.size} files found")
                System.out.println(s"Counting source files... done")
            }
        )

}
