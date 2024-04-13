package mipt.tinkoff.demo

import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.{Differ, Duration, FiberRef, Ref, Runtime, Scope, UIO, Unsafe, ZIO, ZIOAppDefault, ZLayer}

object Demo005 extends ZIOAppDefault {

  // Ref & FiberRef

//  private val tracksDone: Ref[Int] =
//    Unsafe.unsafe { implicit unsafe =>
//      Ref.unsafe.make(0)
//    }
//
//  private val currentTrack: FiberRef[Int] =
//    Unsafe.unsafe { implicit unsafe =>
//      FiberRef.unsafe.make(1)
//    }
//
//  private val tracksDoneEffect: UIO[Ref[Int]] =
//    Ref.make(0)

  // "Тяжёлые" вычисления

  def heavyMetalCalculation(currentTrack: FiberRef[Int], tracksDone: Ref[Int]) =
    currentTrack.locally(5) {
      for {
        _     <- ZIO.logInfo("]-> starting")
        _     <- ZIO.sleep(Duration.fromMillis(1800))
        track <- currentTrack.get
        _     <- ZIO.logInfo(s"]-> mixing $track")
        count <- tracksDone.modify(prev => (prev + 1, prev + 1))
        _     <- ZIO.logInfo(s"]-> counting $count")
      } yield "Manowar" -> "Warlord Returns"
    }

  def hardRockCalculation(currentTrack: FiberRef[Int], tracksDone: Ref[Int]) =
    for {
      _     <- ZIO.logWarning("*-> starting")
      _     <- ZIO.sleep(Duration.fromMillis(500))
      track <- currentTrack.modify(prev => (prev, prev + 1)) // <- Increase counter
      _     <- ZIO.logWarning(s"*-> mixing $track")
      count <- tracksDone.modify(prev => (prev + 1, prev + 1)) // <- Increase counter
      _     <- ZIO.logWarning(s"*-> counting $count")
    } yield "Ozzy Osbourne" -> "Blizzard of Ozz"

  def punkIsNotDeadCalc(currentTrack: FiberRef[Int], tracksDone: Ref[Int]) =
    for {
      _     <- ZIO.logFatal(">-> starting")
      _     <- ZIO.sleep(Duration.fromMillis(500))
      track <- currentTrack.getAndUpdate(_ + 1)
      _     <- ZIO.logFatal(s">-> mixing $track")
      count <- tracksDone.updateAndGet(_ + 1)
      _     <- ZIO.logFatal(s">-> counting $count")
    } yield "Offspring" -> "Smash"

  def powerOfPowerMetal(currentTrack: FiberRef[Int], tracksDone: Ref[Int]) =
    currentTrack.locally(7) {
      for {
        _     <- ZIO.logError("+-> starting")
        _     <- ZIO.sleep(Duration.fromMillis(1100))
        track <- currentTrack.getAndUpdate(_ + 1)
        _     <- ZIO.logError(s"+-> mixing $track")
        count <- tracksDone.modify(prev => (prev + 1, prev + 1))
        _     <- ZIO.logError(s"+-> counting $count")
      } yield "Blind Guardian" -> "Sacred Worlds"
    }

  // сибираем всё вместе

  override def run: ZIO[Scope, Any, Any] = {
    for {
      _ <- ZIO.logDebug("\t> Starting")

//        currentTrack <- FiberRef.make[Int](0)
      tracksDone <- Ref.make(0)

      differ       = Differ.update[Int]
      currentTrack <- FiberRef.makePatch(0, differ)

      fiber1 <- heavyMetalCalculation(currentTrack, tracksDone).fork
      fiber2 <- hardRockCalculation(currentTrack, tracksDone).repeatN(3).fork
      fiber3 <- punkIsNotDeadCalc(currentTrack, tracksDone).repeatN(1).fork
      fiber4 <- powerOfPowerMetal(currentTrack, tracksDone).repeatN(1).fork

      _ <- ZIO.logDebug("\t> Waiting")

      result1 <- fiber1.join
      result2 <- fiber2.join
      result3 <- fiber3.join
      result4 <- fiber4.join

      result = result1 :: result2 :: result3 :: result4 :: Nil

      _     <- ZIO.logDebug(s"\t> Finished: $result")
      total <- tracksDone.get
      last  <- currentTrack.get
    } yield s"$last/$total"
  }.tap(str => ZIO.logDebug(str))

  override val bootstrap: ZLayer[Any, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)

}
