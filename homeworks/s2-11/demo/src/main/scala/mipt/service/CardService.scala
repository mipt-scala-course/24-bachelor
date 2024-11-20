package mipt.service

import cats.MonadThrow
import cats.syntax.all.*

import mipt.cache.CardsCache
import mipt.model.{Card, UserId}
import mipt.external.CardsExternalService

trait CardService[F[_]]:
  def getUserCards(userId: UserId): F[List[Card]]

object CardService:
  private class Impl[F[_]: MonadThrow](
      externalService: CardsExternalService[F],
      cache: CardsCache[F]
  ) extends CardService[F]:

    override def getUserCards(userId: UserId): F[List[Card]] =
      val getAndCacheCards = for
        cards <- externalService.getUserCards(userId)
        _     <- cache.putUserCards(userId, cards).handleError(_ => ())
      yield cards
      getAndCacheCards.handleErrorWith(_ => cache.getUserCards(userId))

  def apply[F[_]: MonadThrow](externalService: CardsExternalService[F], cache: CardsCache[F]): CardService[F] =
    new Impl[F](externalService, cache)
