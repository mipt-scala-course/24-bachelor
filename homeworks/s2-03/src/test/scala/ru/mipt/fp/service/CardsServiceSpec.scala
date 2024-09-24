package ru.mipt.fp.service

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import ru.mipt.fp.cache.Cache
import ru.mipt.fp.external.CardsMasterSystemClient
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, ClientId, Ucid}
import ru.mipt.fp.testdata.TestData._

import scala.util.{Try, Success}

class CardsServiceSpec extends AnyFlatSpec with Matchers with MockFactory:

  "getClientCards" should "return cards from cache if exists" in new Wirings:
    mockCardsListCache.get _ expects clientId returns Success(Some(maskedCards))
    mockCardsListCache.expire _ expects (clientId, ttl) returns Success(())

    service.getClientCards(clientId) shouldEqual Success(maskedCards)

  it should "return cards from external service and cache the list if not exists" in new Wirings:
    mockCardsListCache.get _ expects clientId returns Success(None)
    mockCardsListCache.put _ expects (clientId, maskedCards) returns Success(())
    mockCardsListCache.expire _ expects (clientId, ttl) returns Success(())
    mockCardsClient.getClientCards _ expects clientId returns Success(cards)

    service.getClientCards(clientId) shouldEqual Success(maskedCards)

  "getCardById" should "return card from cache if exists" in new Wirings:
    mockCardCache.get _ expects ucid returns Success(Some(maskedCard))
    mockCardCache.expire _ expects (ucid, ttl) returns Success(())

    service.getCardById(ucid) shouldEqual Success(maskedCard)

  it should "return card from external service and cache the card if not exists" in new Wirings:
    mockCardCache.get _ expects ucid returns Success(None)
    mockCardCache.put _ expects (ucid, maskedCard) returns Success(())
    mockCardCache.expire _ expects (ucid, ttl) returns Success(())
    mockCardsClient.getCard _ expects ucid returns Success(card)

    service.getCardById(ucid) shouldEqual Success(maskedCard)

  "deactivateCard" should "request card deactivation via external service and invalidate cache" in new Wirings:
    mockCardsClient.deactivateCard _ expects ucid returns Success(())
    mockCardCache.invalidate _ expects ucid returns Success(())
    
    service.deactivateCard(ucid) shouldEqual Success(())

  trait Wirings:
    val mockCardsClient    = mock[CardsMasterSystemClient[Try]]
    val mockCardCache      = mock[Cache[Try, Ucid, Card]]
    val mockCardsListCache = mock[Cache[Try, ClientId, List[Card]]]

    val service = new CardService[Try](mockCardsClient, mockCardCache, mockCardsListCache, ttl)
