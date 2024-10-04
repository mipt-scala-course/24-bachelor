package ru.mipt.fp.service

import java.time.Instant

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import ru.mipt.fp.external.{
  CardsMasterSystemClient,
  CustomerMasterSystemClient,
  FastPaymentSystemClient,
  OperationsSystemClient
}
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, ClientId, Ucid}
import ru.mipt.fp.testdata.TestData._

import scala.util.{Try, Success}

class CardsServiceSpec extends AnyFlatSpec with Matchers with MockFactory:

  "getPossibleRecipients" should "return all results from inner storage and fast payment system" in new Wirings:
    mockCustomerMasterSystemClient.seachClientsByPhoneNumber expects phoneNumber returns Success(List(innerCustomer))
    mockFastPaymentSystemClient.searchCustomersByPhoneNumber expects phoneNumber returns Success(outerCustomers)

    service.getPossibleRecipients(phoneNumber) shouldBe Success(allRecipientOptions)

  "getOperationsStatistics" should "return stats for account and cards" in new Wirings:
    val from = Instant.now().minusSeconds(10000)
    val to   = Instant.now()

    mockOperationsSystemClient.getAccountOperations expects (accountId, from, to) returns Success(accountOperations)
    mockOperationsSystemClient.getCardOperations expects (ucid, from, to) returns Success(cardOperations)
    mockCardsClient.getCard expects ucid returns Success(card)

    service.getOperationsStatistics(accountId, List(ucid), from, to) shouldBe Success(allOperationStats)

  trait Wirings:

    val mockCardsClient                = mock[CardsMasterSystemClient[Try]]
    val mockCustomerMasterSystemClient = mock[CustomerMasterSystemClient[Try]]
    val mockFastPaymentSystemClient    = mock[FastPaymentSystemClient[Try]]
    val mockOperationsSystemClient     = mock[OperationsSystemClient[Try]]

    val service = new CardService[Try](
      mockOperationsSystemClient,
      mockCardsClient,
      mockCustomerMasterSystemClient,
      mockFastPaymentSystemClient
    )
