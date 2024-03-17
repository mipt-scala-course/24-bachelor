package mipt.tinkoff.homework.declarative

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Try}

class TIOSpec extends AnyFlatSpec with Matchers {

  import TIOOps._

  "recoverWith" should "recover applicable exception" in {

    TIORuntime.run {
      TIO[Int](throw new ArithmeticException()).recoverWith {
        case _: ArithmeticException => TIO.pure(10)
      }
    } shouldBe 10

    TIORuntime.run {
      TIO.failed[Int](new ArithmeticException()).recoverWith {
        case _: ArithmeticException => TIO.pure(10)
      }
    } shouldBe 10

  }

  it should "throw non-applicable exception" in {

    Try(
      TIORuntime.run {
        TIO[Int](throw new UnsupportedOperationException()).recoverWith {
          case _: ArithmeticException => TIO.pure(10)
        }
      }
    ).failed.get shouldBe a[UnsupportedOperationException]

    Try(
      TIORuntime.run {
        TIO.failed[Int](new UnsupportedOperationException()).recoverWith {
          case _: ArithmeticException => TIO.pure(10)
        }
      }
    ).failed.get shouldBe a[UnsupportedOperationException]

  }

}
