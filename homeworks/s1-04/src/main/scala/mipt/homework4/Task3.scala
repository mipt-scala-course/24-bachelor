package mipt.homework4

import mipt.utils.Homeworks._

/**
  * В этом файле все рекурсивные методы необходимо реализовать через хвостовую рекурсию
  */
object Task3 {

  /**
    * Реализовать тип данных, представляющий натуральные числа (включая 0) через числа Пеано.
    * Num - рекурсивный алгебраический тип данных вида:
    *
    *   Zero ~= 1
    *   Num ~= Zero + Num
    *
    * Соответственно, натуральные числа представляются в виде:
    * 0 = L(Zero)
    * 1 = R(L(Zero))
    * 2 = R(R(L(Zero)))
    * 3 = R(R(R(L(Zero))))
    * ...
    *
    */
  sealed trait Num {

    /**
      * Реализовать метод, возвращающий соответствующее число типа Int
      */
    def toInt: Int = task"Реализуйте метод toInt" (3, 2) // задание (3, 1) в объекте-компаньоне Num

    /**
      * Реализовать метод сравнивающий два натуральных числа
      */
    def ===(arg: Num): Boolean = task"Реализуйте метод ===" (3, 3)

    /**
      * Реализовать метод возвращающий сумму числа с arg
      */
    def +(arg: Num): Num = task"Реализуйте метод +" (3, 4)

    /**
      * Реализовать метод возвращающий разность числа с arg
      * Если число меньше arg, то возвращать None
      */
    def -(arg: Num): Option[Num] = task"Реализуйте метод -" (3, 5)

    /**
      * Реализовать метод возвращающий произведение числа с arg
      */
    def *(arg: Num): Num = task"Реализуйте метод *" (3, 6)

    /**
      * Реализовать метод возвращающий реузльтат целочисленного деления числа на arg
      * Если arg равен Zero, то возвращать None
      */
    def /(arg: Num): Option[Num] = task"Реализуйте метод /" (3, 7)

    /**
      * Реализовать метод возвращающий реузльтат возведения числа в степень arg
      */
    def pow(arg: Num): Num = task"Реализуйте метод pow" (3, 8)
  }

  //  case object Zero extends Num         // ~= 1
  //  case class Succ(...) extends Num     // ~= Num

  object Num {

    /**
      * Реализовать метод возвращающий предаствление Int в виде числа Пеано
      * Если i < 0, то возвращать None
      */
    def fromInt(i: Int): Option[Num] = task"Реализуйте метод fromInt" (3, 1)
  }

}
