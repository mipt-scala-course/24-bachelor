package homework

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline, summonFrom}

trait Eq[A]:
  def eqv(first: A, second: A): Boolean

  extension (first: A)
    def =:=(second: A): Boolean = eqv(first, second)

object Eq extends Instances, Derivation

/**
 *
 *  Реализовать в трейте Instances инстансы Eq для стандартных типов данных:
 *      - Int, Boolean, String
 *
 *      Реализовать вывод инстансов Eq
 *      - для типа Option[A] (имея в контексте Eq[A])
 *
 *      - для типа List[A] (имея в контексте Eq[A])
 */
trait Instances:

  given Eq[Int] = ???
  
  given Eq[String] = ???
  
  given Eq[Boolean] = ???

  given [A: Eq]: Eq[Option[A]] = ???

  given [A: Eq]: Eq[List[A]] = ???

trait Derivation:

  /**
   * 1.Реализовать метод fields
   * case class Boo(x: String, y: Int)
   *
   * Boo("1", 5).fields == List("1", 5)
   */
  extension[T] (value: T)
    def fields(using asProduct: T <:< Product): Iterable[Any] = ???


  /**
   * 2.Реализовать метод summonInstances
   * Возвращает список инстансов Eq[?] для переданных типов, сохраняя порядок.
   * Если какого-то из инстансов нет, то возвращается ошибка компиляции.
   * Для того, чтобы получить инстанс следует использовать summonInline
   *
   * summonInstances[(String, Int, Boolean)] // = List(Eq.given_Eq_String, Eq.given_Eq_Int, Eq.given_Eq_Boolean)
   *
   * summonInstances[(Long, String)] // not compiles
   */
  inline def summonInstances[T <: Tuple]: List[Eq[?]] = ???

  /**
   * 3.Реализовать метод summonOrDeriveChild
   * Если в скоупе есть Eq для данного типа, то вернуть его,
   * иначе если это тип продукт - задерайвить.
   */
  inline def summonOrDeriveChild[Child]: Eq[Child] = ???

  /**
   * 4.Реализовать метод summonOrDeriveChild
   * Возвращает список инстансов Eq[?] для переданных типов, сохраняя порядок.
   * Если какого-то из инстансов нет и это тип продукт - то он должен задерайвиться
   */
  inline def summonOrDeriveChilds[Elems <: Tuple]: List[Eq[?]] = ???
  
  /**
   * 5.Реализовать метод check
   * Метод должны сравнивать два объекта переданным инстансом Eq
   */
  def check(x: Any, y: Any, eq: Eq[?]): Boolean = ???


  /**
   * 6. Реализовать метод eqProduct
   * Метод должны сравнивать два объекта одного типа продукта используя переданные инстансы
   * case class Boo(x: String, y: Int)
   * given Eq[Boo] = Eq.eqProduct[Boo](List(Eq.given_Eq_String, Eq.given_Eq_Int))
   * Boo("1", 1) =:= Boo("1", 1) // true
   */
  def eqProduct[T](instances: => List[Eq[?]])(using asProduct: T <:< Product): Eq[T] = ???

  /**
   * 7.Реализовать метод eqSum
   * Метод должны сравнивать два объекта одного типа суммы используя переданные инстансы
   *
   * enum Foo:
   *   case Bar(y: String)
   *   case Baz(x: Int)
   *
   * given Eq[Foo] = Eq.eqSum[Foo](List(Eq.given_Eq_Bar, Eq.given_Eq_Baz))
   *
   * (Foo.Baz(1): Foo) =:= (Foo.Bar("1"): Foo) // false
   * (Foo.Baz(1): Foo) =:= (Foo.Baz(1): Foo) // true
   */
  def eqSum[T](instances: => List[Eq[?]])(using m: Mirror.SumOf[T]): Eq[T] = ???

  /**
   * 7.Реализовать метод derived используя методы выше
   */
  inline def derived[T](using m: Mirror.Of[T]): Eq[T] = ???