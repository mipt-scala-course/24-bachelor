package mipt.tinkoff.demo.p08_zio

import zio._
import scala.util.Try


object ZioWhenAppDemo extends ZIOAppDefault {

  override def run: ZIO[Any, Throwable, String] =
    for {
      _       <- Console.printLine("Who are you?")
      name    <- Console.readLine
      _       <- Console.printLine("O! Really?")
      ack     <- Console.readLine
      confirm <- ZIO.when(ack.toLowerCase == "yes")(ZIO.succeed(name))
      result  <- ZIO.getOrFailWith(new Exception("Inadequate"))(confirm)
      _       <- Console.printLine(s"Hello, $result!")
    } yield result

}


object ZioForeachDemo extends ZIOAppDefault {

  def divideTry(division: Long, divisor: Int) =
    ZIO.fromTry(Try(division / divisor.toLong))

  def divideEither(division: Long, divisor: Int) =
    ZIO.fromEither(
      if (divisor == 0)
        Left(new Exception("divide by zero"))
      else
        Right(division / divisor.toLong)
    )

  def divideZio(division: Long, divisor: Int) =
    ZIO.attempt(division / divisor.toLong)


  override def run =
    divideTry(10, 0)
      .orElse(divideEither(11, 2))
      .orElse(divideZio(12, 1))
      .catchAll(ex => Console.printLine("Something goes bad :(") *> ZIO.fail(ex))
      .foldZIO(
        ex  => Console.printLineError(ex.getMessage) *> ZIO.succeed(0),
        res => Console.printLine(s"Success: $res!").as(res)
      )
      .forEachZIO(result => Console.printLine(result.toString))

}

