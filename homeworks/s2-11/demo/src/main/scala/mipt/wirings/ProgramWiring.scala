package mipt.wirings

import cats.effect.{Async, Resource}
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import dev.profunktor.redis4cats.log4cats.*
import mipt.cache.CardsCache
import mipt.external.CardsExternalService
import mipt.service.{CardMaskingImpl, CardService}
import org.http4s.blaze.client.BlazeClientBuilder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object ProgramWiring:

  def redis[F[_]: Async](redisUri: String): Resource[F, RedisCommands[F, String, String]] =
    given Logger[F] = Slf4jLogger.getLogger[F]

    Redis[F].utf8(redisUri)

  def wire[F[_]: Async](externalServiceUri: String, redisUri: String): Resource[F, CardService[F]] =
    for
      httpClient     <- BlazeClientBuilder[F].resource
      externalService = CardsExternalService(httpClient, externalServiceUri)
      redisCommands  <- redis(redisUri)
      cache           = CardsCache(redisCommands)
      service         = CardService(externalService, cache, CardMaskingImpl)
    yield service
