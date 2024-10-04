package ru.mipt.fp.testdata

import ru.mipt.fp.domain.{
  AccountId,
  BankId,
  Card,
  CardCvv,
  CardExpirationDate,
  CardNumber,
  CardOperationStats,
  ClientId,
  Customer,
  FastPaymentsSystemCustomer,
  Operation,
  OperationStats,
  PaymentRecipient,
  PhoneNumber,
  Ucid
}

import scala.concurrent.duration.*

object TestData {

  val clientId          = ClientId("1234567890")
  val accountId         = AccountId("1234567890")
  val accountOperations = List(
    Operation(clientId, 200),
    Operation(clientId, -100),
    Operation(clientId, -1200),
    Operation(clientId, 1100)
  )

  val ucid              = Ucid("Ucid")
  val cardNumber        = CardNumber("1234-5678-9012-3456")
  val cvv               = CardCvv("123")
  val expirationDate    = CardExpirationDate("01/20")
  val card              = Card(ucid, cardNumber, cvv, expirationDate)
  val maskedCardNumber  = CardNumber("1234-****-****-*456")
  val cardOperations    = List(
    Operation(clientId, 300),
    Operation(clientId, 500),
    Operation(clientId, 800)
  )
  val allOperationStats = OperationStats(
    income = 1300,
    outcome = -1300,
    cardOperations = List(
      CardOperationStats(maskedCardNumber, 1600, 0)
    )
  )

  val phoneNumber          = PhoneNumber("+79991234567")
  val innerMainPhoneNumber = PhoneNumber("+79997654321")
  val innerCustomer        = Customer("Василий", "Чапаев", innerMainPhoneNumber)
  val outerCustomers       = List(
    FastPaymentsSystemCustomer("Василий Ч.", BankId("Сбер")),
    FastPaymentsSystemCustomer("Василий Ч.", BankId("ВТБ"))
  )
  val allRecipientOptions  = List(
    PaymentRecipient(innerMainPhoneNumber, "Василий Ч.", BankId.TBank),
    PaymentRecipient(phoneNumber, "Василий Ч.", BankId("Сбер")),
    PaymentRecipient(phoneNumber, "Василий Ч.", BankId("ВТБ"))
  )

}
