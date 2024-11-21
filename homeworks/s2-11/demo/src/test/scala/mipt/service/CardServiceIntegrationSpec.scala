package mipt.service

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.syntax.all.*
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{MockServerContainer, RedisContainer}
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import dev.profunktor.redis4cats.RedisCommands
import io.circe.syntax.*
import mipt.testdata.CardsTestData._
import mipt.utils.MockServerClientWrapper
import mipt.wirings.ProgramWiring
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class CardServiceIntegrationSpec extends AsyncFlatSpec with Matchers with TestContainersForAll with AsyncIOSpec:

  override type Containers = MockServerContainer and RedisContainer

  "getUserCards" should "return cards from external service and put them to cache for fallback" in
    testEnvironment { (mockServer, redis, service) =>
      for
        _ <- MockServerClientWrapper.mockGetCards(
               mockServer,
               userId,
               cards.asJson.noSpaces
             )
        _ <- service.getUserCards(userId).map(_ shouldBe cards)
        _ <- redis.get(userId).map(_ shouldBe Some(cards.asJson.noSpaces))
      yield ()
    }

  it should "return cards from external service and update cache with them for fallback" in
    testEnvironment { (mockServer, redis, service) =>
      for
        _ <- redis.set(anotherUserId, cards.asJson.noSpaces)
        _ <- MockServerClientWrapper.mockGetCards(
               mockServer,
               anotherUserId,
               anotherCards.asJson.noSpaces
             )
        _ <- service.getUserCards(anotherUserId).map(_ shouldBe anotherCards)
        _ <- redis.get(anotherUserId).map(_ shouldBe Some(anotherCards.asJson.noSpaces))
      yield ()
    }

  override def startContainers(): Containers =
    val mockServer = MockServerContainer.Def("5.15.0").start()
    val redis      = RedisContainer.Def().start()

    mockServer and redis

  def testEnvironment(
      f: (MockServerContainer, RedisCommands[IO, String, String], CardService[IO]) => IO[Unit]
  ): IO[Unit] =
    withContainers { case mockServer and redis =>
      val redisResource   = ProgramWiring.redis[IO](redis.redisUri)
      val serviceResource = ProgramWiring.wire[IO](mockServer.endpoint, redis.redisUri)
      (redisResource product serviceResource).use { case (redis, service) =>
        f(mockServer, redis, service)
      }
    }
