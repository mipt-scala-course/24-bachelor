package mipt.tinkoff.demo.p08_zio

import zio._


object sc04_zlayer extends ZIOAppDefault {

  // String producer
  trait Producer {
    def produce: String
  }

  object Producer {
    val default = new Producer {
      override def produce: String =
        "Hello, World!"
    }
  }


  // String to Long modifier
  trait Modifier {
    def modify(input: String): Long
  }

  object Modifier {
    val live: ZIO[Any, Nothing, Modifier] =
      ZIO.succeed(
      new Modifier {
        override def modify(input: String): Long =
          input.length
      }
    )
  }


  // Long consumer
  trait Consumer {
    def consume(division: Long): Task[Double]
  }

  object Consumer {
    def createFor(divisor: Int) =
      new Consumer {
        override def consume(division: Long): Task[Double] =
          ZIO.attempt(division.toDouble/divisor)
      }
  }


  // Value registry
  trait Registry {
    def registerString(value: String): Task[Unit]
    def registerLong  (value: Long)  : Task[Unit]
    def registerDouble(value: Double): Task[Unit]
  }

  object Registry {
    class ConsoleRegistry extends Registry {

      override def registerString(value: String): Task[Unit] =
        Console.printLine(s"Registering $value")

      override def registerLong(value: Long): Task[Unit] =
        Console.printLine(s"Registering $value")

      override def registerDouble(value: Double): Task[Unit] =
        Console.printLine(s"Registering $value")
    }
  }


  // Result Auditing
  trait Audit {
    def auditValue(result: Double): Task[Double]
  }

  object Audit {
    def connectTo(registry: Registry): Audit =
      new Audit {
        override def auditValue(result: Double): Task[Double] =
          ZIO.ifZIO(ZIO.succeed(result < 1))(
            onTrue  = registry.registerString("Audit successful").as(result),
            onFalse = ZIO.fail(new Exception("Too Long"))
          )
      }
  }


  val zio: ZIO[Audit with Consumer with Modifier with Producer, Throwable, Unit] =
    for {
      produced <- ZIO.service[Producer].map(_.produce)
      modified <- ZIO.serviceWith[Modifier](_.modify(produced))
      consumed <- ZIO.serviceWithZIO[Consumer](_.consume(modified))
      passed   <- ZIO.serviceWithZIO[Audit](_.auditValue(consumed))
      _        <- Console.printLine(s"Final result = $passed")
    } yield ()



  // Program
  override val run =
    zio
      .catchAll(
        th => Console.printLineError(th.getMessage)
      )
      .provide(
        ZLayer.succeed[Producer](Producer.default),
        ZLayer.fromZIO(Modifier.live),
        ZLayer.succeed[Int](17),
        ZLayer.fromFunction[Int => Consumer](Consumer.createFor),
        ZLayer.fromFunction[Registry => Audit](Audit.connectTo),
        ZLayer.succeed[Registry](new Registry.ConsoleRegistry),
        ZLayer.Debug.mermaid
      )

  val funcLayer: ZLayer[Int, Nothing, Consumer] =
    ZLayer.fromFunction[Int => Consumer](Consumer.createFor)


}
