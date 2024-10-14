package ru.mipt.fp.dao

import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}

trait CardServiceDao:
  def getCardByUcid(ucid: Ucid)(config: Config): Option[Card]

  def createCard(
      ucid: Ucid,
      number: CardNumber,
      cvv: CardCvv,
      expirationDate: CardExpirationDate
  )(config: Config): Option[Unit]

  def updateCard(
      ucid: Ucid,
      newNumber: Option[CardNumber],
      newCvv: Option[CardCvv],
      newExpirationDate: Option[CardExpirationDate]
  )(config: Config): Option[Card]

  def deleteCard(ucid: Ucid)(config: Config): Option[Unit]
