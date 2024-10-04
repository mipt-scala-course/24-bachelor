package ru.mipt.fp.service

import java.time.Instant

import ru.mipt.fp.domain.{
  AccountId,
  BankId,
  OperationStats,
  PaymentRecipient,
  PhoneNumber,
  Ucid
}
import ru.mipt.fp.external.{
  CardsMasterSystemClient,
  CustomerMasterSystemClient,
  FastPaymentSystemClient,
  OperationsSystemClient
}

class CardService[F[_]](
    operationsSystemClient: OperationsSystemClient[F],
    cardsMasterSystemClient: CardsMasterSystemClient[F],
    customerMasterSystemClient: CustomerMasterSystemClient[F],
    fastPaymentSystemClient: FastPaymentSystemClient[F],
):

  /** Запросить список возможных получателей платежа по номеру телефона
    *
    * Поиск выполнять в нашей базе клиентов и в системе быстрых платежей
    *
    * Имя клиента должно возвращаться в формате, как его возвращает СБП, то есть например "Иван И."
    */
  def getPossibleRecipients(phoneNumber: PhoneNumber): F[List[PaymentRecipient]] = ???

  /** Запросить статистику по операциям по счету и выбранным картам за указанный период
    *
    * Статистику сгруппировать по доходным и расходным операциям
    *
    * Номер карты должен быть маскирован следующим образом: 1234-****-****-*789
    */
  def getOperationsStatistics(
      accountId: AccountId,
      cardIds: List[Ucid],
      from: Instant,
      to: Instant
  ): F[OperationStats] = ???
