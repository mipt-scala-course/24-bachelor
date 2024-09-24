package ru.mipt.fp.external

import ru.mipt.fp.domain.{Card, ClientId, Ucid}

/** Интерфейс к мастер-системе данных карт.
  */
trait CardsMasterSystemClient[F[_]]:

  def getClientCards(clientId: ClientId): F[List[Card]]

  def getCard(cardId: Ucid): F[Card]

  def deactivateCard(cardId: Ucid): F[Unit]
