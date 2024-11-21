package mipt.service

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import mipt.cache.CardsCache
import mipt.external.CardsExternalService
import mipt.testdata.CardsTestData._
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class CardServiceSpec extends AsyncFlatSpec with Matchers with AsyncMockFactory with AsyncIOSpec:

  "getUserCards" should "return cards from external service and put them to cache for fallback" in
    testEnvironment { env =>
      import env._

      externalService.getUserCards expects userId returning IO(cards)
      cards.foreach(card => cardMasking.mask expects card returning card)
      cache.putUserCards expects (userId, cards) returning IO(())

      service.getUserCards(userId).map(_ shouldBe cards)
    }

  it should "not fail if external service is available but cache is not" in
    testEnvironment { env =>
      import env._

      externalService.getUserCards expects userId returning IO(cards)
      cards.foreach(card => cardMasking.mask expects card returning card)
      cache.putUserCards expects (userId, cards) returning IO.raiseError(
        new RuntimeException("Cache is not available")
      )

      service.getUserCards(userId).map(_ shouldBe cards)
    }

  it should "return cards from fallback cache if external service is unavailable" in
    testEnvironment { env =>
      import env._

      externalService.getUserCards expects userId returning IO.raiseError(
        new RuntimeException("Database is unavailable")
      )
      cache.getUserCards expects userId returning IO(cards)

      service.getUserCards(userId).map(_ shouldBe cards)
    }

  def testEnvironment(f: TestEnvironment => IO[Unit]): IO[Unit] = f(new TestEnvironment {})

  trait TestEnvironment:
    val externalService = mock[CardsExternalService[IO]]
    val cache           = mock[CardsCache[IO]]
    val cardMasking     = mock[CardMasking]
    val service         = CardService(externalService, cache, cardMasking)
