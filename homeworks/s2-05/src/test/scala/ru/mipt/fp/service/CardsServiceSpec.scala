package ru.mipt.fp.service

import java.time.Instant

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import ru.mipt.fp.dao.{CardServiceDao, Config}
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, ClientId, Ucid}
import ru.mipt.fp.testdata.TestData._

import scala.util.{Try, Success}

class CardsServiceSpec extends AnyFlatSpec with Matchers with MockFactory:
  "getCardByUcid" should "return a correct card data" in new Wirings {
    (mockCardsServiceDao.getCardByUcid(_: Ucid)(_: Config)) expects (ucid, config) returns Some(card)

    service.getCardByUcid(ucid)(config).value shouldBe Success(Some(card))
  }

  it should "return none if a data base response is empty" in new Wirings {
    (mockCardsServiceDao.getCardByUcid(_: Ucid)(_: Config)) expects (ucid, config) returns None

    service.getCardByUcid(ucid)(config).value shouldBe Success(None)
  }

  "createCard" should "successfully create a card" in new Wirings {
    (mockCardsServiceDao.createCard(_: Ucid, _: CardNumber, _: CardCvv, _: CardExpirationDate)(_: Config)) expects
      (ucid, cardNumber, cvv, expirationDate, config) returns Some(())

    service.createCard(ucid, cardNumber, cvv, expirationDate)(config).value shouldBe Success(Some(()))
  }

  it should "return none if nothing was created" in new Wirings {
    (mockCardsServiceDao.createCard(_: Ucid, _: CardNumber, _: CardCvv, _: CardExpirationDate)(_: Config)) expects
      (ucid, cardNumber, cvv, expirationDate, config) returns None

    service.createCard(ucid, cardNumber, cvv, expirationDate)(config).value shouldBe Success(None)
  }

  "updateCard" should "return an updated card" in new Wirings {
    (mockCardsServiceDao.updateCard(_: Ucid, _: Option[CardNumber], _: Option[CardCvv], _: Option[CardExpirationDate])(
      _: Config
    )) expects (ucid, Some(newCardNumber), Some(newCvv), Some(newExpirationDate), config) returns Some(updatedCard)

    service.updateCard(ucid, Some(newCardNumber), Some(newCvv), Some(newExpirationDate))(config).value shouldBe
      Success(Some(updatedCard))
  }

  it should "return none if there was no update" in new Wirings {
    (mockCardsServiceDao.updateCard(_: Ucid, _: Option[CardNumber], _: Option[CardCvv], _: Option[CardExpirationDate])(
      _: Config
    )) expects (ucid, None, None, None, config) returns None

    service.updateCard(ucid, None, None, None)(config).value shouldBe Success(None)
  }

  "deleteCard" should "successfully delete a card" in new Wirings {
    (mockCardsServiceDao.deleteCard(_: Ucid)(_: Config)) expects (ucid, config) returns Some(())

    service.deleteCard(ucid)(config).value shouldBe Success(Some(()))
  }

  it should "do nothing if there was no deletion" in new Wirings {
    (mockCardsServiceDao.deleteCard(_: Ucid)(_: Config)) expects (ucid, config) returns None

    service.deleteCard(ucid)(config).value shouldBe Success(None)
  }

  trait Wirings:
    val mockCardsServiceDao = mock[CardServiceDao]
    val service             = new CardService[Try](mockCardsServiceDao)
