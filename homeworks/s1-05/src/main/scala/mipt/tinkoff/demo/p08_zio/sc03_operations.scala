package mipt.tinkoff.demo.p08_zio

import zio._


object Common {

  val map = Map(9 -> 'Z', 8 -> 'Y', 7 -> 'X')
  val seq = (1 to 3).map(_.toLong * 500)

  val success = ZIO.succeed(123)
  val another = ZIO.fromOption(Some("ABC"))
  val failure = ZIO.fail(new Exception("Boom!"))
  val longrun = ZIO.attempt( seq.foreach(Thread.sleep) )

}


object Recovering extends ZIOAppDefault {
  import Common._

  override val run = success

}


object Sugared extends ZIOAppDefault {
  import Common._

  val flatmapped1 = success  *> another
  val flatmapped2 = success <*  another
  val flatmapped3 = success <*> another
  val tapped = success.tap(value => Console.printLine(value.toString))

  val foreached1 = flatmapped3.forEachZIO(value => Console.printLine(value))

  override val run =
    for {
      first  <- success
      second <- another
      third  <- failure.foldZIO( _ => longrun, ZIO.succeed)
      result  = s"$first, $second, $third"
      _ <- Console.printLine(result)
    } yield ()

}


object Collecting extends ZIOAppDefault {
  import Common._

  val collect = ZIO.collect(map) { case tuple => ZIO.succeed(tuple) }

  val foreached1 = ZIO.foreach(seq)(value => ZIO.succeed(value))

  def hardWork(latency: Long) = {
    println(s"> start $latency")
    Thread.sleep(latency)
    println(s"* stop $latency")
  }
  val foreached2 = ZIO.foreachPar(seq)(value => ZIO.attempt(hardWork(value)))
  val raced = success.raceFirst(failure)

  override val run = foreached2

}


object Scheduling extends ZIOAppDefault {
  import Common._

  val effect =
    success
      .delay(1.second)
      .tap(_ => Console.printLine("delayed"))
      .repeatN(3)
      .timeoutFail(new Exception("Timeout"))(3000.millis)

  override val run =
    effect.catchAll(err => Console.printLineError(err.getMessage))

}