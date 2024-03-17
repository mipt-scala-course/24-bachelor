package mipt.tinkoff.homework
package executable

import Homeworks._
import scala.util.{Failure, Success, Try}

/**
 * У нас есть реализация своей IO-монады TIO в виде функционального эффекта с исполняемой моделью.
 * TIO представляет собой обертку вокруг функции, к которой есть smart-конструкторы и операторы map и flatMap,
 * и этот эффект пригоден для описания последовательного выполнения каких-либо чистых операций или действий с побочными эффектам
 * на базе стандартного Scala Try, фактически являясь ленивой версией Try
 *
 * Нужно реализовать возможность заворачивать в этот эффект и обрабатывать также возможные исключения
 * Для этого нужно реализовать smart-конструктор failed и оператор recoverWith с указанной семантикой
 *
 * Обратите внимание на PartialFunction в сигнатуре recoverWith:
 * PartialFunction[A, B] отличается от Function[A, B] тем, что может быть определена не на всём множестве аргументов A
 * и должна исполняться только тогда, когда partialFunction.isDefinedAt(a) возвращает true
 * Пример:
 * // val pf: PartialFunction[String, int] = {
 * //     case "Roses are red" => 11
 * //     case "Return of the warlord" = 18
 * // }
 * //
 * // def usage(string: String) =
 * //   if (pf.isDefinedAt(string)) pf(string) else 0
 *
 * Так же обратите внимание на метод partialFunction.applyOrElse
 */
class TIO[T](val run: () => Try[T])

object TIO {

  def pure[T](v: T): TIO[T] =
    new TIO(() => Success(v))

  def failed[A](th: Throwable): TIO[A] =
    task"Имплементируйте создание упавшего с исключением TIO"(2, 1)


  def apply[T](block: => T): TIO[T] =
    new TIO(() => Try(block))

}

object TIOOps {

  implicit class ops[T](val effect: TIO[T]) extends AnyVal {

    def map[U](f: T => U): TIO[U] =
      new TIO(() => effect.run().map(f))

    def flatMap[U](f: T => TIO[U]): TIO[U] =
      new TIO(() => effect.run().flatMap { t => f(t).run() })

    /**
     * Имплементируйте метод восстановления TIO из ошибочного состояния со следующим поведением:
     * если переданная функция определена для имеющегося исключения, то
     *   результат метода rocoverWith должен быть результатом выполнения указанной функции
     * если переданная функция не определена для имеющегося исключения, то
     *   метод recoverWith не должен ничего делать
     */
    def recoverWith(pf: PartialFunction[Throwable, TIO[T]]): TIO[T] =
      task"""
         Имплементируйте метод восстановления TIO из ошибочного состояния со следующим поведением:
         если переданная функция определена для имеющегося исключения, то
           результат метода rocoverWith должен быть результатом выполнения указанной функции
         если переданная функция не определена для имеющегося исключения, то
           метод recoverWith не должен ничего делать
      """(2, 2)

  }

}

