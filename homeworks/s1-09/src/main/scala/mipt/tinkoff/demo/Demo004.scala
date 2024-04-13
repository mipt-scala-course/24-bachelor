package mipt.tinkoff.demo

import zio.{Duration, Exit, Fiber, Runtime, Scope, ZIO, ZIOAppDefault, ZLayer}
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

import java.io.IOException

object Demo004 extends ZIOAppDefault {

  val heavyMetalCalculation: ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logDebug("]-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- ZIO.logDebug("]-> finishing")
    } yield "Warlord returns"

  val hardRockWork: ZIO[Any, Nothing, String] =
    for {
      _ <- ZIO.logWarning("-*> starting")
      _ <- ZIO.sleep(Duration.fromMillis(500))
      _ <- ZIO.logWarning("-*> finishing")
    } yield "Crazy train"

  val punkIsNotDead =
    for {
      _ <- ZIO.logFatal("}*> starting")
      _ <- ZIO.sleep(Duration.fromMillis(3000))
      _ <- ZIO.logFatal("}*> finishing")
    } yield "Offspring" -> "Smash"

  val powerOfPowerMetal =
    for {
      _ <- ZIO.logTrace("+*> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1500))
      _ <- ZIO.logTrace("+*> finishing")
    } yield "Blind Guardian" -> "Sacred Worlds"

  val rockAndRollBabe =
    for {
      _ <- ZIO.logError("+*> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1500))
      _ <- ZIO.logError("+*> finishing")
    } yield "The Rolling Stones" -> "Jumpin’ Jack Flash"

  val common =
    for {
      _ <- ZIO.logInfo("Starting")

      //      fiberMetal: Fiber[IOException, Int] <- heavyMetalCalculation.fork
      fiberMetal <- heavyMetalCalculation.fork
      fiberRocky <- hardRockWork.repeatN(3).forkScoped
      fiberPunk  <- punkIsNotDead.fork
      fiberPower <- powerOfPowerMetal.repeatN(3).fork
      fiberRolla <- rockAndRollBabe.repeatN(3).forkDaemon

      _ <- ZIO.logInfo("Waiting")

      metal <- fiberMetal.join
      rocky <- fiberRocky.await

      _ <- rocky match {
            case Exit.Success(value) => ZIO.logInfo("")
            case Exit.Failure(cause) => ZIO.logInfo("")
          }
      punkStatus <- fiberPunk.status
      _          <- ZIO.logInfo(s"Punk is $punkStatus")
      _          <- ZIO.sleep(Duration.fromMillis(100))
      punks      <- fiberPunk.interrupt

      //        power <- fiberPower.join
      //        rnrbb <- fiberRolla.join

      _ <- ZIO.logInfo(
            s""" $metal
           | with rocky
           | with $punks
           | with power
           | and rnrbb
           |""".stripMargin
          )
    } yield ()

  override def run: ZIO[Scope, Any, Any] =
    ZIO.scoped {
      for {
        f <- common.fork
//      _ <- ZIO.sleep(Duration.fromMillis(3000))
        r <- f.join
        _ <- ZIO.sleep(Duration.fromMillis(2000))
      } yield r
    } *> ZIO.sleep(Duration.fromMillis(2000))

  override val bootstrap: ZLayer[Any, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)

}
