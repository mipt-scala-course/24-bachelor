package homework

import scala.deriving.Mirror
import scala.quoted.{Expr, Quotes, Type}


object Derivation:
  inline def deriveEqForProduct[T: Mirror.ProductOf]: Eq[T] = ${ deriveEqForProductMacro[T] }

  inline def deriveEqForSum[T: Mirror.SumOf]: Eq[T] = ${ deriveEqForSumMacro[T] }

  private def deriveEqForProductMacro[T: Type](using quotes: Quotes): Expr[Eq[T]] =
    new DerivationMacro(using quotes).deriveEqForProduct[T]

  private def deriveEqForSumMacro[T: Type](using quotes: Quotes): Expr[Eq[T]] =
    new DerivationMacro(using quotes).deriveEqForSum[T]


private class DerivationMacro(using val quotes: Quotes):
  import quotes.reflect.*

  /**
   *  1. Задерайвить ненайденные Eq используя deriveMissingDistinctInstances.
   *  2. Создать Block с соответствующими Statement и в нем же вернуть реализацию Eq[T]
   *  3. Реализация Eq[T] должна быть типизирована, за исключением реализации метода eqv.
   *
   *  Чтобы получить тип поля кейс класса следует на TypeRepr использовать метод memberType
   */
  def deriveEqForProduct[T: Type]: Expr[Eq[T]] =
    '{
      new Eq[T]:
        override def eqv(first: T, second: T): Boolean = false
    }

  /**
   *  Необходимо сравнить поля двух инстансов кейс класса попарно используя найденные инстансы Eq для каждого поля
   *  Затем вернуть выражение вида firstFieldEqv && secondFieldEqv && thirdFieldEqv && ...
   *  Для того, чтобы выбрать поле, необходимо использовать метод select.
   */
  def productFieldsEquality[T: Type](
    first: Expr[T],
    second: Expr[T],
    derivedInstances: Map[TypeRepr, Ref]
  ): Expr[Boolean] =
    val equalityPerField: List[Expr[Boolean]] = ???
    Expr(false)


  /**
   *  1. Задерайвить ненайденные Eq используя deriveMissingDistinctInstances.
   *  2. Создать блок с соответствующими Statement и в нем же вернуть реализацию Eq[T]
   */
  def deriveEqForSum[T: Type]: Expr[Eq[T]] =
    val children = getAllChildren(TypeRepr.of[T])
    '{
      new Eq[T]:
        override def eqv(first: T, second: T): Boolean = false
    }

  /**
   *  Создать pattern match вида:
   *  (first, second) match
   *    case (first: FirstType, second: FirstType) => // сравнение найденным Eq
   *    case (first: SecondType, second: SecondType) => //сравнение найденным Eq
   *    ...
   *    case _ => false
   *
   */
  private def matchByTypeAndCheckEquality[T: Type](
    first: Expr[T],
    second: Expr[T],
    types: List[TypeRepr],
    derivedInstances: Map[TypeRepr, Ref]
  ): Expr[Boolean] =
    Expr(false)



  /*
    Возвращает определения задерайвленных Eq, а также ссылки на соответствующие определения.
    Если Eq уже существует, то дерайвить не нужно.
    Если есть несколько одинаковых типов, то определение должно быть только одно.
  */
  private def deriveMissingDistinctInstances(types: List[TypeRepr]): (List[ValDef], Map[TypeRepr, Ref]) =
    val instances = distinct(types).flatMap { tpe =>
      Option.empty[(ValDef, (TypeRepr, Ref))]
    }
    (instances.map(_._1), instances.map(_._2).toMap)

  /* Возвращает уникальный список типов */
  private def distinct(tpes: List[TypeRepr]): List[TypeRepr] =
    tpes.foldLeft(List.empty[TypeRepr]) { (acc, tpe) =>
      if (acc.exists(_ =:= tpe)) acc
      else tpe :: acc
    }



  /* Возвращает типы детей типа суммы */
  private def getAllChildren(tpe: TypeRepr): List[TypeRepr] =
    tpe.asType match
      case '[t] =>
        Expr.summon[scala.deriving.Mirror.Of[t]] match
          case Some('{ $m: scala.deriving.Mirror.SumOf[t] {type MirroredElemTypes = subs} }) =>
            typeReprsOf[subs].flatMap(getAllChildren)
          case _ =>
            List(tpe)

  /* Преобразует Tuple типов в список */
  private def typeReprsOf[Ts: Type]: List[TypeRepr] =
    Type.of[Ts] match
      case '[EmptyTuple] => Nil
      case '[t *: ts]    => TypeRepr.of[t] :: typeReprsOf[ts]


