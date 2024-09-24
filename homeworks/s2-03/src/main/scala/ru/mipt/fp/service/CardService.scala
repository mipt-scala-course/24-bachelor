package ru.mipt.fp.service

import cats.FlatMap
import cats.syntax.all.*
import ru.mipt.fp.cache.Cache
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, ClientId, Ucid}
import ru.mipt.fp.masking.Masking
import ru.mipt.fp.external.CardsMasterSystemClient

import scala.concurrent.duration.FiniteDuration

class CardService[F[_]: FlatMap](
    cardsClient: CardsMasterSystemClient[F],
    cardsCache: Cache[F, Ucid, Card],
    cardsListCache: Cache[F, ClientId, List[Card]],
    cacheTtl: FiniteDuration
):

  /** Запросить данные всех карт текущего пользователя, маскировать чувствительные данные и вернуть весь список
    *
    * Номер карты из 4444-4444-4444-4567 должен стать 4444-****-****-*567
    *
    * Срок действия и CVV должны замениться на '**\**' и *** соответственно
    *
    * Чтение должно быть кэширующим Из соображений безопасности в кэше не должно быть немаскированных данных по карте
    *
    * Кэширование должно производиться с TTL, указанным в конструкторе
    *
    * При чтении данных из кэша время жизни должно продлеваться
    */
  def getClientCards(clientId: ClientId): F[List[Card]] = ???

  /** Запросить данные карты из внешнего хранилища по ее Ucid, выполнить маскирование и вернуть информацию
    *
    * Номер карты из 4444-4444-4444-4567 должен стать 4444-****-****-*567
    *
    * Срок действия и CVV должны замениться на **\** и *** соответственно
    *
    * Чтение должно быть кэширующим Из соображений безопасности в кэше не должно быть немаскированных данных по карте
    *
    * Кэширование должно производиться с TTL, указанным в конструкторе
    *
    * При чтении данных из кэша время жизни должно продлеваться
    */
  def getCardById(ucid: Ucid): F[Card] = ???

  /** Запросить деактивацию карты по ее Ucid
    *
    * Запрос должен инвалидировать кэш для этой карты
    */
  def deactivateCard(ucid: Ucid): F[Unit] = ???
