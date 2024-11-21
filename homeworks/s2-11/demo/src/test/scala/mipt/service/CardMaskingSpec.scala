package mipt.service

import mipt.testdata.CardsTestData._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardMaskingSpec extends AnyFlatSpec with Matchers:

  "mask" should "return a copy of Card object with masked card number" in {
    CardMaskingImpl.mask(card) shouldBe maskedCard
  }
