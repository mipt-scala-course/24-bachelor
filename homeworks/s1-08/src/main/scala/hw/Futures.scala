//> using scala 2.13
package hw

import scala.concurrent.{ExecutionContext, Future}

object Futures {

  /**
   * Реализуйте функцию, которая выполнит свертку (fold) входящей последовательности из Future,
   * используя переданный комбинатор и начальное значение для свертки.
   * Если какая-либо из исходных Future зафейлилась, то должна вернуться ошибка от нее
   */
  def foldF[A, B](in: Seq[Future[A]], zero: B, op: (B, A) => B)(
    implicit executionContext: ExecutionContext
  ): Future[B] =
    // /*
    Future.foldLeft(in)(zero)(op)
    // */

  /**
   * Реализуйте функцию, которая выполнит свертку (fold) входящей последовательности из Future,
   * используя переданный асинхронный комбинатор и начальное значение для свертки.
   * Если какая-либо из исходных Future зафейлилась, то должна вернуться ошибка от нее.
   * Если комбинатор зафейлился, то должна вернуться ошибка от него.
   */
  def flatFoldF[A, B](in: Seq[Future[A]], zero: B, op: (B, A) => Future[B])(
    implicit executionContext: ExecutionContext
  ): Future[B] =
    // /*
    in.foldLeft(Future.successful(zero)) { (resF, elF) =>
      resF.zip(elF).flatMap(op.tupled)
    }
    // */

  /**
   * В данном задании Вам предлагается реализовать функцию fullSequence,
   * похожую на Future.sequence, но в отличии от нее,
   * возвращающую все успешные и не успешные результаты.
   * Возвращаемое тип функции - кортеж из двух списков,
   * в левом хранятся результаты успешных выполнений,
   * в правово результаты неуспешных выполнений.
   * Не допускается использование методов объекта Await и мутабельных переменных var
   */
  def fullSequence[A](futures: List[Future[A]])(
    implicit ex: ExecutionContext
  ): Future[(List[A], List[Throwable])] =
    // /*
    futures.foldLeft(Future.successful((List.empty[A], List.empty[Throwable]))) { (resF, f) =>
      resF.flatMap { case (successes, fails) =>
        f.map(l => (l :: successes, fails)).recover {
          case th => (successes, th :: fails)
        }
      }
    }.map { case (s, f) => (s.reverse, f.reverse) }
    // */

  /**
   * Реализуйте traverse c помощью метода Future.sequence
   */
  def traverse[A, B](in: List[A])(fn: A => Future[B])(
    implicit ex: ExecutionContext
  ): Future[List[B]] =
    // /*
    Future.sequence(in.map(fn))
    // */

  /**
   * Реализуйте алгоритм map/reduce.
   * Исходный список обрабатывается параллельно (конкурентно) с помощью применения функции map к каждому элементу
   * Результаты работы функции map должны быть свернуты в одно значение функцией reduce
   * Если в ходе выполнения какой-либо операции возникло исключение - эту обработку нужно игнорировать
   * Если ни один вызов map не завершился успешно, вернуть зафейленную фьючу с исключением UnsupportedOperationException
   */
  def mapReduce[A, B, B1 >: B](in: List[A], map: A => Future[B], reduce: (B1, B1) => B1)(
    implicit ex: ExecutionContext
  ): Future[B1] =
    // /*
    Future.traverse(in) { a =>
      map(a).map(Some(_)).recover { case _ => None }
    }.map(_.flatten).map(_.reduce(reduce))
    // */

}
