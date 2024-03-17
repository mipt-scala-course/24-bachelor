package mipt.tinkoff.homework.executable

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success, Try}

class TIOSpec extends AnyFlatSpec with Matchers {

  import TIOOps._

  "recoverWith" should "recover applicable exception" in {

    TIO.pure(10).map(_ / 0).recoverWith {
      case _: ArithmeticException => TIO.pure(10)
    }.run() shouldBe Success(10)

  }

  it should "throw non-applicable exception" in {

    TIO.pure(10).map(_ / 0).recoverWith {
      case _: UnsupportedOperationException => TIO.pure(10)
    }.run().failed.get shouldBe a[ArithmeticException]

  }

}
