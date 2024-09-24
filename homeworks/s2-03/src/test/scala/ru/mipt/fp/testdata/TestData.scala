package ru.mipt.fp.testdata

import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, ClientId, Ucid}

import scala.concurrent.duration._

object TestData {

  val ttl      = 1.second
  val clientId = ClientId("Client")

  val ucid           = Ucid("Ucid")
  val cardNumber     = CardNumber("1234-5678-9012-3456")
  val cvv            = CardCvv("123")
  val expirationDate = CardExpirationDate("01/20")
  val card           = Card(ucid, cardNumber, cvv, expirationDate)

  val cards = List(card)

  val maskedCardNumber     = CardNumber("1234-****-****-*456")
  val maskedCvv            = CardCvv("***")
  val maskedExpirationDate = CardExpirationDate("**\\**")
  val maskedCard           = Card(ucid, maskedCardNumber, maskedCvv, maskedExpirationDate)

  val maskedCards = List(maskedCard)

}
