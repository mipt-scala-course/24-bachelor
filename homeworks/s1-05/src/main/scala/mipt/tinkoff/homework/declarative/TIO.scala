package mipt.tinkoff.homework
package declarative

import Homeworks._
import scala.util.{Failure, Success, Try}

/**
 * У нас есть реализация своей IO-монады TIO в виде функционального эффекта с декларативной моделью.
 * TIO представляет собой ADT, к которому есть smart-конструкторы и операторы map и flatMap,
 * и этот эффект пригоден для описания последовательного выполнения каких-либо чистых операций или действий с побочными эффектам
 *
 * Нужно реализовать возможность заворачивать в этот эффект и обрабатывать также возможные исключения
 * Для этого нужно дополнить модель, реализовать smart-конструктор failed и оператор recoverWith
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
sealed trait TIO[+T]

case class Complete[A](value: () => A)                         extends TIO[A]
case class CalculationStep[A, B](f: A => TIO[B], prev: TIO[A]) extends TIO[B]


object TIO {

  def pure[A](v: A): TIO[A] =
    Complete(() => v)

  def failed[A](th: Throwable): TIO[A] =
    task"Имплементируйте создание упавшего с исключением TIO"(1, 1)


  def apply[A](block: => A): TIO[A] =
    CalculationStep[Unit, A](_ => pure(block), pure(()))

}

object TIOOps {

  implicit class ops[A](val io: TIO[A]) extends AnyVal {

    def map[B](f: A => B): TIO[B]          =
      flatMap(a => TIO.pure(f(a)))

    def flatMap[B](f: A => TIO[B]): TIO[B] =
      CalculationStep(f, io)

    def recoverWith(pf: PartialFunction[Throwable, TIO[A]]): TIO[A] =
      task"""
        Имплементируйте метод восстановления TIO из ошибочного состояния со следующим поведением:
        если переданная функция определена для имеющегося исключения, то
          результат метода rocoverWith должен быть результатом выполнения указанной функции
        если переданная функция не определена для имеющегося исключения, то
          метод recoverWith не должен ничего делать
      """(1, 2)

  }

}

object TIORuntime {

  /**
   * Допишите интерпретатор так, чтобы при выполнении он обрабатывал восстановление из ошибки
   * Можете доработать один из case, если потребуется
   */
  def run[A](io: TIO[A]): A = io match {
    case Complete(value)          =>
      value()
    case CalculationStep(f, prev) =>
      run(f(run(prev)))
  }

}
