package mipt.tinkoff.demo

import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.{Console, Duration, Promise, Runtime, Scope, ZIO, ZIOAppDefault, ZLayer}

import java.io.IOException

object Demo006 extends ZIOAppDefault {

  // Представим, что у нас есть три тяжёлых вычисления

  def heavyMetalSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logInfo("]-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- ZIO.logInfo("]-> finishing")
      _ <- sync.succeed("Warlord returns" -> "Warriors of the World")
    } yield "Manovar"

  def hardRockSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logWarning("*-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- ZIO.uninterruptible {
            (ZIO.sleep(Duration.fromMillis(100)) *> ZIO.interruptible(Console.print("."))).repeatN(100)
          }
      _ <- ZIO.logWarning("*-> finishing")
      _ <- sync.succeed("Blizzard of Ozz" -> "Crazy train")
    } yield "Ozzy Osbourne"

  def punkRockSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logError(">-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(2000))
      _ <- ZIO.logError(">-> finishing")
      _ <- sync.succeed("Smash" -> "Gotta get away")
    } yield "Offspring"

  // Собираем их вместе

  val common =
    for {
      _ <- ZIO.logDebug("\t> Starting")

      sync <- Promise.make[Nothing, (String, String)]

      fiber1 <- heavyMetalSync(sync).repeatN(15).fork
      fiber2 <- hardRockSync(sync).repeatN(30).fork
      fiber3 <- punkRockSync(sync).repeatN(20).forkDaemon

      _ <- ZIO.logDebug("\t> Waiting")

      result <- sync.await

//                _ <- ZIO.sleep(Duration.fromMillis(1800))
//                _ <- sync.succeed("" -> "")

      state1 <- fiber1.interrupt
      state2 <- fiber2.interrupt
      state3 <- fiber3.interrupt

      _ <- ZIO.logDebug(s"\t> Finished: $result")
    } yield result

  // Запускаем

  override def run: ZIO[Any, Any, Any] = {
    for {
      f <- common.fork
      _ <- ZIO.sleep(Duration.fromMillis(2500))
      _ <- Console.printLine("BEFORE")
      _ <- f.interrupt
      _ <- Console.printLine("AFTER")
    } yield "" -> ""
//    ZIO.scoped {
//      (for {
//        f <- common.fork
//        r <- f.join
//      } yield r) <* ZIO.sleep(Duration.fromMillis(1500))
//    }
  }.tap(_ => ZIO.sleep(Duration.fromSeconds(3)))
    .flatMap(result => Console.printLine(s"'${result._1}'' with '${result._2}'") *> ZIO.sleep(Duration.fromSeconds(3)))

  override val bootstrap: ZLayer[Any, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)

}
