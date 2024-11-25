package mipt.service

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.syntax.all.*
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{MockServerContainer, RedisContainer, ToxiproxyContainer}
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import dev.profunktor.redis4cats.RedisCommands
import io.circe.syntax.*
import mipt.testdata.CardsTestData.*
import mipt.utils.MockServerClientWrapper
import mipt.wirings.ProgramWiring
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.Network

class CardServiceIntegrationSpec extends AsyncFlatSpec with Matchers with TestContainersForAll with AsyncIOSpec:

  override type Containers = MockServerContainer and RedisContainer and ToxiproxyContainer

  "getUserCards" should "return cards from external service and put them to cache for fallback" in
    testEnvironment { (mockServer, redis, service) =>
      for
        _ <- MockServerClientWrapper.mockGetCards(
               mockServer,
               userId,
               cards.asJson.noSpaces
             )
        _ <- service.getUserCards(userId).map(_ shouldBe cardsResponse)
        _ <- redis.get(userId).map(_ shouldBe Some(cardsResponse.asJson.noSpaces))
      yield ()
    }

  it should "return cards from fallback cache if external service is failed" in
    testEnvironment { (mockServer, redis, service) =>
      for
        _ <- redis.set(anotherUserId, anotherCardsResponse.asJson.noSpaces)
        _ <- MockServerClientWrapper.mockFailGetCards(mockServer, anotherUserId)
        _ <- service.getUserCards(anotherUserId).map(_ shouldBe anotherCardsResponse)
        _ <- redis.get(anotherUserId).map(_ shouldBe Some(anotherCardsResponse.asJson.noSpaces))
      yield ()
    }

  it should "return cards from external service or empty list if is fails and skip Redis fails" in
    testEnvironmentWithProxy { (mockServer, redisProxy, service) =>
      for
        _ <- MockServerClientWrapper.mockGetCards(
               mockServer,
               userId,
               cards.asJson.noSpaces
             )
        _ <- IO(redisProxy.setConnectionCut(true))

        _ <- service.getUserCards(userId).map(_ shouldBe cardsResponse)

        _ <- MockServerClientWrapper.mockFailGetCards(mockServer, anotherUserId)
        _ <- service.getUserCards(anotherUserId).map(_ shouldBe List.empty)

        _ <- IO(redisProxy.setConnectionCut(false))
      yield ()
    }

  override def startContainers(): Containers =
    val mockServer = MockServerContainer.Def("5.15.0").start()
    val network    = Network.newNetwork()
    val redis      = new RedisContainer("redis:latest")
      .configure(
        _.withNetwork(network)
          .withExposedPorts(6379)
          .withNetworkAliases("redis")
      )
    val toxiproxy  = new ToxiproxyContainer("shopify/toxiproxy:2.1.4")
      .configure(_.withNetwork(network))

    redis.start()
    toxiproxy.start()

    mockServer and redis and toxiproxy

  def testEnvironment(
      f: (MockServerContainer, RedisCommands[IO, String, String], CardService[IO]) => IO[Unit]
  ): IO[Unit] =
    withContainers { case mockServer and redis and _ =>
      val redisResource   = ProgramWiring.redis[IO](redis.redisUri)
      val serviceResource = ProgramWiring.wire[IO](mockServer.endpoint, redis.redisUri)
      (redisResource product serviceResource).use { case (redis, service) =>
        f(mockServer, redis, service)
      }
    }

  def testEnvironmentWithProxy(
      f: (MockServerContainer, ToxiproxyContainer.ContainerProxy, CardService[IO]) => IO[Unit]
  ): IO[Unit] =
    withContainers { case mockServer and _ and _ =>
      val redisUrl        = s"redis://${redisProxy.getContainerIpAddress}:${redisProxy.getProxyPort}"
      val serviceResource = ProgramWiring.wire[IO](mockServer.endpoint, redisUrl)
      serviceResource.use { service =>
        f(mockServer, redisProxy, service)
      }
    }

  private lazy val redisProxy = withContainers { case _ and _ and toxiproxy =>
    toxiproxy.proxy("redis", 6379)
  }
