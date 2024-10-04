package ru.mipt.fp.domain

object BankId:
  opaque type T = String
  def apply(value: String): T = value

  val TBank: BankId = "TBank"

type BankId = BankId.T

case class PaymentRecipient(phoneNumber: PhoneNumber, name: String, bankId: BankId)
