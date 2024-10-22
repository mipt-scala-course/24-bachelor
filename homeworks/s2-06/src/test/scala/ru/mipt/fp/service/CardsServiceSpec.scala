package ru.mipt.fp.service

import cats.data.ReaderT
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.mipt.fp.dao.{CardServiceDao, DaoConfig}
import ru.mipt.fp.dao.CardServiceDao.{AlreadyExists, ConnectionTimedOut, DaoError, NotFound}
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}
import ru.mipt.fp.service.CardService.Config
import ru.mipt.fp.testdata.TestData._
import ru.mipt.fp.util.{Random, Retry, Timer}

import java.time.Instant
import scala.util.{Try, Success}

class CardsServiceSpec extends AnyFlatSpec with Matchers with MockFactory:
  "getCardByUcid" should "return a correct card data" in new Wirings {
    (mockCardsServiceDao.getCardByUcid(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns Right(card)

    service.getCardByUcid(ucid)(config) shouldBe Right(card)
  }

  it should "return none if a data base response is empty" in new Wirings {
    (mockCardsServiceDao.getCardByUcid(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns Left(NotFound(ucid))

    service.getCardByUcid(ucid)(config) shouldBe Left(NotFound(ucid))
  }

  it should "retry a connection timed out error" in new Wirings {
    (mockCardsServiceDao.getCardByUcid(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns
      Left(ConnectionTimedOut) repeat 6

    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(1))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(2))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(3))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(4))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(5))
    timer.sleep expects 1 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 2 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 3 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 4 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 5 returns ReaderT((_: Config) => Right(()))

    service.getCardByUcid(ucid)(config) shouldBe Left(ConnectionTimedOut)
  }

  "createCard" should "successfully create a card" in new Wirings {
    (mockCardsServiceDao.createCard(_: Ucid, _: CardNumber, _: CardCvv, _: CardExpirationDate)(_: DaoConfig)) expects
      (ucid, cardNumber, cvv, expirationDate, daoConfig) returns Right(())

    service.createCard(ucid, cardNumber, cvv, expirationDate)(config) shouldBe Right(())
  }

  it should "return none if nothing was created" in new Wirings {
    (mockCardsServiceDao.createCard(_: Ucid, _: CardNumber, _: CardCvv, _: CardExpirationDate)(_: DaoConfig)) expects
      (ucid, cardNumber, cvv, expirationDate, daoConfig) returns Left(AlreadyExists(ucid))

    service.createCard(ucid, cardNumber, cvv, expirationDate)(config) shouldBe Left(AlreadyExists(ucid))
  }

  it should "retry a connection timed out error" in new Wirings {
    (mockCardsServiceDao.createCard(_: Ucid, _: CardNumber, _: CardCvv, _: CardExpirationDate)(_: DaoConfig)) expects
      (ucid, cardNumber, cvv, expirationDate, daoConfig) returns Left(ConnectionTimedOut) repeat 6

    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(1))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(2))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(3))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(4))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(5))
    timer.sleep expects 1 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 2 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 3 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 4 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 5 returns ReaderT((_: Config) => Right(()))

    service.createCard(ucid, cardNumber, cvv, expirationDate)(config) shouldBe Left(ConnectionTimedOut)
  }

  "updateCard" should "return an updated card" in new Wirings {
    (mockCardsServiceDao.updateCard(_: Ucid, _: Option[CardNumber], _: Option[CardCvv], _: Option[CardExpirationDate])(
      _: DaoConfig
    )) expects (ucid, Some(newCardNumber), Some(newCvv), Some(newExpirationDate), daoConfig) returns Right(updatedCard)

    service.updateCard(ucid, Some(newCardNumber), Some(newCvv), Some(newExpirationDate))(config) shouldBe
      Right(updatedCard)
  }

  it should "return none if there was no update" in new Wirings {
    (mockCardsServiceDao.updateCard(_: Ucid, _: Option[CardNumber], _: Option[CardCvv], _: Option[CardExpirationDate])(
      _: DaoConfig
    )) expects (ucid, None, None, None, daoConfig) returns Left(NotFound(ucid))

    service.updateCard(ucid, None, None, None)(config) shouldBe Left(NotFound(ucid))
  }

  it should "retry a connection timed out error" in new Wirings {
    (mockCardsServiceDao.updateCard(_: Ucid, _: Option[CardNumber], _: Option[CardCvv], _: Option[CardExpirationDate])(
      _: DaoConfig
    )) expects (ucid, None, None, None, daoConfig) returns Left(ConnectionTimedOut) repeat 6

    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(1))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(2))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(3))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(4))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(5))
    timer.sleep expects 1 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 2 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 3 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 4 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 5 returns ReaderT((_: Config) => Right(()))

    service.updateCard(ucid, None, None, None)(config) shouldBe Left(ConnectionTimedOut)
  }

  "deleteCard" should "successfully delete a card" in new Wirings {
    (mockCardsServiceDao.deleteCard(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns Right(())

    service.deleteCard(ucid)(config) shouldBe Right(())
  }

  it should "do nothing if there was no deletion" in new Wirings {
    (mockCardsServiceDao.deleteCard(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns Left(NotFound(ucid))

    service.deleteCard(ucid)(config) shouldBe Left(NotFound(ucid))
  }

  it should "retry a connection timed out error" in new Wirings {
    (mockCardsServiceDao.deleteCard(_: Ucid)(_: DaoConfig)) expects (ucid, daoConfig) returns
      Left(ConnectionTimedOut) repeat 6

    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(1))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(2))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(3))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(4))
    random.betweenLong expects (*, *) returns ReaderT((_: Config) => Right(5))
    timer.sleep expects 1 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 2 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 3 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 4 returns ReaderT((_: Config) => Right(()))
    timer.sleep expects 5 returns ReaderT((_: Config) => Right(()))

    service.deleteCard(ucid)(config) shouldBe Left(ConnectionTimedOut)
  }

  trait Wirings:
    type F[A] = ReaderT[Either[DaoError, *], Config, A]

    given timer: Timer[F]   = mock[Timer[F]]
    given random: Random[F] = mock[Random[F]]

    given Retry[F, DaoError] = new Retry[F, DaoError]

    val mockCardsServiceDao = mock[CardServiceDao]
    val service             = new CardService[F](mockCardsServiceDao)
