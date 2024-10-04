package ru.mipt.fp.external

import ru.mipt.fp.domain.{Card, Ucid}

/** Интерфейс к мастер-системе данных карт.
  */
trait CardsMasterSystemClient[F[_]]:

  def getCard(cardId: Ucid): F[Card]
