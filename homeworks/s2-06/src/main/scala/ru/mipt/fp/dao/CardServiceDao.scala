package ru.mipt.fp.dao

import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}
import ru.mipt.fp.dao.CardServiceDao.{CreateError, DeleteError, GetError, UpdateError}

trait CardServiceDao:
  def getCardByUcid(ucid: Ucid)(config: DaoConfig): Either[GetError, Card]

  def createCard(
      ucid: Ucid,
      number: CardNumber,
      cvv: CardCvv,
      expirationDate: CardExpirationDate
  )(config: DaoConfig): Either[CreateError, Unit]

  def updateCard(
      ucid: Ucid,
      newNumber: Option[CardNumber],
      newCvv: Option[CardCvv],
      newExpirationDate: Option[CardExpirationDate]
  )(config: DaoConfig): Either[UpdateError, Card]

  def deleteCard(ucid: Ucid)(config: DaoConfig): Either[DeleteError, Unit]

object CardServiceDao:
  case object ConnectionTimedOut
  case class NotFound(ucid: Ucid)

  type GetError = ConnectionTimedOut.type | NotFound

  case class AlreadyExists(ucid: Ucid)

  type CreateError = ConnectionTimedOut.type | AlreadyExists
  type UpdateError = ConnectionTimedOut.type | NotFound
  type DeleteError = ConnectionTimedOut.type | NotFound

  type DaoError = GetError | CreateError | UpdateError | DeleteError
