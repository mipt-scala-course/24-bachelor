package ru.mipt.fp.external

import java.time.Instant
import ru.mipt.fp.domain.{AccountId, Operation, Ucid}

import scala.concurrent.duration.FiniteDuration

/** Интерфейс к системе данных по операциям
  */
trait OperationsSystemClient[F[_]]:
  def getCardOperations(cardId: Ucid, from: Instant, to: Instant): F[List[Operation]]
  def getAccountOperations(accountId: AccountId, from: Instant, to: Instant): F[List[Operation]]
