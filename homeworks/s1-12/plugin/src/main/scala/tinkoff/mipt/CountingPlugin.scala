package tinkoff.mipt

import sbt._
import Keys._
import complete.DefaultParsers._

object CountingPlugin extends AutoPlugin {
    
    override def trigger = allRequirements
    override def requires = empty

    object autoImport {
        val countSourceFiles = taskKey[Unit]("Count source files")
        val countSourceLines = inputKey[Unit]("Count source lines")
    }
    import autoImport._


    override def projectSettings: Seq[Def.Setting[_]] =
        inConfig(Compile)(countingSettings)

    lazy val countingSettings: Seq[Def.Setting[_]] =
        Seq(
            countSourceFiles := {
                //TODO: получить список файлов исходных текстов и вывести в лог их количество
            },
            countSourceLines := {
                //TODO: 
				// 1. Проверит наличие аргументоы команды. Если аргументов не т - вывести error в лог
				// 2. Взять первый аргумент и найти файл с таким именем. Если файл не найдет вывести warn в лог.
				// 3. Подсчитать число строк в файле и вывести info в лог. Для чтения из файла вам пригодится объект IO.
            }
        )

}