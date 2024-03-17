package mipt.tinkoff.demo
package p08_zio

import scala.util.{Failure, Success, Try}




// DI

object RuntimeIdea extends App {
  import p07_declarative._

  object InOutRuntime {
    import mipt.tinkoff.demo.p07_declarative.InOutSystem._

    sealed trait InOutError

    final case class InOutConsoleError(cause: String) extends InOutError
    final case class GeneralInOutError(cause: String) extends InOutError

    // ---

    trait InOutConsole[E, S] {
      def printLine(line: => S): InOut[E, Unit]
      def readLine             : InOut[E, S]
    }


    object DefaultInOutConsole extends InOutConsole[InOutError, String] {

      override def printLine(line: => String): InOut[InOutConsoleError, Unit] =
        InOut.pure(println(line))

      override def readLine: InOut[InOutConsoleError, String] =
        Try(scala.io.StdIn.readLine()) match {
          case Success(value) =>
            InOut.pure(value)
          case Failure(error) =>
            InOut.rise(InOutConsoleError(error.getMessage))
        }
    }

  }


  import mipt.tinkoff.demo.p07_declarative.InOutSystem._
  import InOutRuntime._


  trait InOutProgram[-R, +E, +A] extends (R => InOut[E, A])


  val helloProgram =
    new InOutProgram[InOutConsole[InOutError, String], InOutError, String] {

      override def apply(console: InOutConsole[InOutError, String]): InOut[InOutError, String] =
        for {
          _       <- console.printLine("Who are you?")
          name    <- console.readLine
          _       <- console.printLine("O!, Really?")
          confirm <- console.readLine
          result  <- confirm.toLowerCase() match {
            case "yes" => InOut.pure(name)
            case _     => InOut.rise[InOutError](GeneralInOutError("Inadequate"))
          }
          _       <- console.printLine(s"Hello, $result!")
        } yield result

    }

  println(helloProgram)
  println(helloProgram(DefaultInOutConsole))
  println("And again")
  println(helloProgram(DefaultInOutConsole))

}






// ---


// ---




object NaiveDemo extends App {

  import zio._

  val helloApp: ZIO[Any, Throwable, String] =
    for {
      _      <- Console.printLine("Who are you?")
      name   <- Console.readLine
      _      <- Console.printLine("O! Really?")
      ack    <- Console.readLine
      result <- ack.toLowerCase match {
                  case "yes" => ZIO.succeed(name)
                  case _     => ZIO.fail(new Exception("Inadequate"))
                }
      _      <- Console.printLine(s"Hello, $result!")
    } yield result

  val runtime = Runtime.default

  println(runtime)
  println()

  val result = Unsafe.unsafe { implicit unsafe =>
    runtime.unsafe.run(helloApp)
  }

  println()
  println(result)


}


object ZioAppDemo extends zio.ZIOAppDefault {

  import zio._

  override def run: ZIO[Any, Throwable, String] =
    for {
      _       <- Console.printLine("Who are you?")
      name    <- Console.readLine
      _       <- Console.printLine("O! Really?")
      ack     <- Console.readLine
      result  <- ack.toLowerCase match {
                    case "yes"  => ZIO.succeed(name)
                    case _      => ZIO.fail(new Exception("Inadequate"))
                  }
      _       <- Console.printLine(s"Hello, $result!")
    } yield result

}
