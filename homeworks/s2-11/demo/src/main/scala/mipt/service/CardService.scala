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
      cache: CardsCache[F],
      masking: CardMasking
  ) extends CardService[F]:

    override def getUserCards(userId: UserId): F[List[Card]] =
      val getAndCacheCards = for
        cards      <- externalService.getUserCards(userId)
        maskedCards = cards.map(masking.mask)
        _          <- cache.putUserCards(userId, maskedCards).handleError(_ => ())
      yield maskedCards
      getAndCacheCards
        .handleErrorWith(_ => cache.getUserCards(userId))
        .handleError(_ => List.empty)

  def apply[F[_]: MonadThrow](
      externalService: CardsExternalService[F],
      cache: CardsCache[F],
      masking: CardMasking
  ): CardService[F] =
    new Impl[F](externalService, cache, masking)
