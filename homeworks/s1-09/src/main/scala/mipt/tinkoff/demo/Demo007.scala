package mipt.tinkoff.demo

import zio.concurrent._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.{Console, Dequeue, Duration, Hub, Runtime, Task, ZIO, ZIOAppDefault, ZLayer}

import java.io.IOException

object Demo007 extends ZIOAppDefault {

  def heavyMetalProducer(retrans: Hub[(String, String)], latch: CountdownLatch): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logInfo("]-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- retrans.publish("Louder Than Hell" -> "Return of the Warlord")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- retrans.publish("Warriors of the World" -> "Valhalla")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(1800))
      _ <- retrans.publish("Kings of metal" -> "Sting of the Bumblebee")
      _ <- latch.countDown
      _ <- ZIO.logInfo("]-> finishing")
      _ <- latch.countDown
    } yield "Manovar"

  def hardRockProducer(retrans: Hub[(String, String)], latch: CountdownLatch): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logWarning("*-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(1500))
      _ <- retrans.publish("Blizzard of Ozz" -> "Crazy train")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(1500))
      _ <- retrans.publish("No More Tears" -> "Mama, I'm Coming Home")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(1500))
      _ <- retrans.publish("Down to Earth" -> "Dreamer")
      _ <- latch.countDown
      _ <- ZIO.logWarning("*-> finishing")
    } yield "Ozzy Osbourne"

  def punkRockProducer(retrans: Hub[(String, String)], latch: CountdownLatch): ZIO[Any, IOException, String] =
    for {
      _ <- ZIO.logError(">-> starting")
      _ <- ZIO.sleep(Duration.fromMillis(2000))
      _ <- retrans.publish("Smash" -> "Gotta get away")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(2000))
      _ <- retrans.publish("Americana" -> "Why Don't You Get A Job")
      _ <- latch.countDown
      _ <- ZIO.sleep(Duration.fromMillis(2000))
      _ <- retrans.publish("Conspiracy of One" -> "Want You Bad")
      _ <- latch.countDown
      _ <- ZIO.logError(">-> finishing")
    } yield "Offspring"

  def djSubscriber(retrans: Hub[(String, String)]) = {
    def go(queue: Dequeue[(String, String)]): Task[Unit] =
      for {
        track <- queue.take
        _     <- Console.printLine(s"""mixing "${track._2}"" from "${track._1}"""")
        _     <- go(queue)
      } yield ()

    for {
      dequeue <- retrans.subscribe
      _       <- go(dequeue)
    } yield ()
  }

  override def run =
    for {
      _ <- Console.printLine("\t> Starting")

      hub   <- Hub.bounded[(String, String)](3)
      latch <- CountdownLatch.make(5)

      djfiber <- djSubscriber(hub).fork

      fiber1 <- heavyMetalProducer(hub, latch).fork
      fiber2 <- hardRockProducer(hub, latch).fork
      fiber3 <- punkRockProducer(hub, latch).fork

      _ <- Console.printLine("\t> Waiting")

      _ <- latch.await
      _ <- djfiber.interrupt

      _ <- Console.printLine("\t> Finished")
    } yield ()

  override val bootstrap: ZLayer[Any, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)

}
