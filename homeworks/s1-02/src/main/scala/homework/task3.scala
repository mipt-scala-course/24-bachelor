package homework

import utils.Homeworks._

object task3 {

  sealed trait Ternary
  object Ternary {
    case object Yes   extends Ternary
    case object No    extends Ternary
    case object Maybe extends Ternary
  }

  /**
    * Класс определяет эквивалентность типов
    * Для этого он получает функции прямого и обратного преобразований.
    * Необходимо передавать такие функции, что бы соблюдались правила:
    *
    * Для любого x, to(from(x)) = x
    * Для любого y, from(to(y)) = y
    *
    * @param to функция прямого преобразования
    * @param from функция обратного преобразования
    */
  class Equivalent[A, B](val to: A => B)(val from: B => A)
  object Equivalent {
    def apply[A, B](to: A => B)(from: B => A) =
      new Equivalent[A, B](to)(from)
  }

  /*
   * Решите следующие задания.
   * Вам необходимо реализовать функции прямого и обратного преобразований так,
   * что бы для инициализированного ими Equivalent выполнялись условия эквивалентности
   */

  task"определите эквивалентность экспоненты в булевый тип и произведения 3 ^ 2 = 3 * 3 "
  def boolToThree: Equivalent[Boolean => Ternary, (Ternary, Ternary)] = {
    def to(f: Boolean => Ternary): (Ternary, Ternary) = (f(true), f(false))
    def from(pair: (Ternary, Ternary)): Boolean => Ternary = {
      case true => pair._1
      case false => pair._2
    }
    Equivalent(to)(from)
  }

  task"определите эквивалентность экспоненты в тернарный тип и произведения 2 ^ 3 = 2 * 2 * 2"
  def threeToBool: Equivalent[Ternary => Boolean, (Boolean, Boolean, Boolean)] = {
    def to(f: Ternary => Boolean): (Boolean, Boolean, Boolean) = (f(Ternary.Yes), f(Ternary.No), f(Ternary.Maybe))

    def from(tuple: (Boolean, Boolean, Boolean)): Ternary => Boolean = {
      case Ternary.Yes => tuple._1
      case Ternary.No => tuple._2
      case Ternary.Maybe => tuple._3
    }
    Equivalent(to)(from)
  }

  task"определите эквивалентность двух экпонент высшего порядка булевых типов (2 ^ 2) ^ 2 = 2 ^ (2 ^ 2)"
  def boolToBoolToBool: Equivalent[Boolean => Boolean => Boolean, (Boolean => Boolean) => Boolean] = {
//    def to(f: Boolean => Boolean => Boolean): (Boolean => Boolean) => Boolean =
//      (a: Boolean => Boolean) => {
//        if (a(true)) {
//          f(a(true))(true)
//        } else {
//          f(a(true))(false)
//        }
//      }
//    def from(f: (Boolean => Boolean) => Boolean): Boolean => Boolean => Boolean =
//      _ => (b: Boolean) => f(_ => b)
//
//    Equivalent(to)(from)
    ???
  }

  task"докажите тривиальное качество терминального типа Unit 1 ^ A = 1"
  def AToUnit[A]: Equivalent[A => Unit, Unit] = {
    def to(f: A => Unit): Unit = (a: A) => f(a)
    def from(f: Unit): A => Unit = _ => f

    Equivalent(to)(from)
  }

  task"докажите тривиальность экспоненты с единичным показателем A ^ 1 = A"
  def unitToA[A]: Equivalent[Unit => A, A] = {
    def to(f: Unit => A): A = f()
    def from(a: A): Unit => A = _ => a

    Equivalent(to)(from)
  }

  task"докажите фундаментальное качество паттерн-матчинга C ^ (A + B) = C ^ A * C ^ B"
  def patternMatching[A, B, C]: Equivalent[(A Either B) => C, (A => C, B => C)] = ???

}
