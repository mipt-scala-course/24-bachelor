package homework

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline, summonFrom}

trait Eq[A]:
  def eqv(first: A, second: A): Boolean

  extension (first: A)
    def =:=(second: A): Boolean = eqv(first, second)

object Eq extends Instances:
  inline def derived[T](using m: Mirror.Of[T]): Eq[T] =
    inline m match
      case given Mirror.SumOf[T] =>
        Derivation.deriveEqForSum[T]
        
      case given Mirror.ProductOf[T] =>
        Derivation.deriveEqForProduct[T]
