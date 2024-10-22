package ru.mipt.fp.testdata

import ru.mipt.fp.dao.DaoConfig
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}
import ru.mipt.fp.service.CardService.{Config, ServiceConfig}

import scala.concurrent.duration.*

object TestData {
  val ucid           = Ucid("Ucid")
  val cardNumber     = CardNumber("1234-5678-9012-3456")
  val cvv            = CardCvv("123")
  val expirationDate = CardExpirationDate("01/20")
  val card           = Card(ucid, cardNumber, cvv, expirationDate)

  val newCardNumber     = CardNumber("0987-6543-2109-8765")
  val newCvv            = CardCvv("321")
  val newExpirationDate = CardExpirationDate("12/24")
  val updatedCard       = Card(ucid, newCardNumber, newCvv, newExpirationDate)

  val daoConfig     = DaoConfig("host", "port", "login", "password")
  val serviceConfig = ServiceConfig(5, 150, 250)
  val config        = Config(serviceConfig, daoConfig)
}
