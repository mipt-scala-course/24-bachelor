package mipt.tinkoff.demo.p07_declarative

import scala.util.{Failure, Success, Try}


object CosmoZoo {


  // Option

  case class ExecutiveOptionProgram[+A](
    action: () => Option[A]
  )

  trait OptionProgram
  def interpretO[A](prog: OptionProgram): Option[A] = ???


  // Try

  case class ExecutiveTryProgram[+A](
    action: () => Try[A]
  )

  trait TryProgram
  def interpretT[A](prog: TryProgram): Try[A] = ???


  // Either

  case class ExecutiveEitherProgram[+E, +A](
    action: () => Either[E, A]
  )

  trait EitherProgram
  def interpretE[E, A](prog: EitherProgram): Either[E, A] = ???


  // F[E, A]

  case class ExecutiveProgramF[F[+_, +_], +E, +A](
    action: () => F[E, A]
  )

  trait ProgramF
  def interpretF[F[_, _], E, A](prog: ProgramF): F[E, A] = ???

}






// ---

// ---


/**
 * Демонстрационный(гибридный) вариант.
 * В основе декларативный подход, но смешанный с исполняемым.
 * Из-за смешения сломана ленивость.
 * В дальнейшем мы ее починим применяя всё этот же InOut.
 */
object InOutSystem {

  // 1. domain

  sealed trait InOut[+E, +A] {
    self =>

    override def toString: String =
      self match {
        case InOutSuccess(eval) =>
          s"InOutSuccess(${eval()})"
        case InOutFailure(err) =>
          s"InOutFailure(${err.toString()})"
      }

  }

  final case class InOutSuccess[A](value: () => A)
    extends InOut[Nothing, A]

  final case class InOutFailure[E](error: E)
    extends InOut[E, Nothing]


//  final case class Map[E, A, B](
//    inOut: InOut[E, A],
//    f: A => B
//  ) extends InOut[E, B]
//
//  final case class FlatMap[+E, E1 <: E, E2 <: E, A, +B](
//    inOut:  InOut[E1, A],
//    f: A => InOut[E2, B]
//  ) extends InOut[E, B]


  // 2. constructors

  object InOut {
    def pure[A](value: => A): InOut[Nothing, A] =
      InOutSuccess(() => value)

    def rise[E](error: => E): InOut[E, Nothing] =
      InOutFailure(error)
  }


  // 3. operators

  implicit class InOutOps[E, A](inOut: InOut[E, A]) {


//    def flatMap[EX >: E, E1 <: EX, B](f: A => InOut[E1, B]): InOut[EX, B] =
//      FlatMap[EX, E, E1, A, B](inOut, f)
//
//    def map[B](f: A => B): InOut[E, B] =
//      Map[E, A, B](inOut, f)


    def flatMap[B](f: A => InOut[E, B]): InOut[E, B] =
      inOut match {
        case InOutSuccess(value) =>
          f(value())
        case failure: InOutFailure[E] =>
          failure
      }

    def map[B](f: A => B): InOut[E, B] =
      flatMap(a => InOutSuccess(() => f(a)))

  }

}


object InOutWithErrorDemo extends App {
  import InOutSystem._

  val program: InOut[Throwable, String] =
    for {
      _       <- InOut.pure(println("Who are you?"))
      name    <- Try(scala.io.StdIn.readLine()).fold(err => InOut.rise(err), value => InOut.pure(value))
      _       <- InOut.pure(println("O!, Really?"))
      confirm <- Try(scala.io.StdIn.readLine()).fold(err => InOut.rise(err), value => InOut.pure(value))
      result  <- confirm.toLowerCase() match {
                  case "yes" => InOut.pure(name)
                  case _     => InOut.rise(new Exception("Inadequate"))
                }
      _       <- InOut.pure(println(s"Hello, $confirm!"))
    } yield result


  println(program.toString())
  println(program.toString())

}








// ---

// ---

