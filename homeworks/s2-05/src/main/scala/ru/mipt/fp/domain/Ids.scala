package ru.mipt.fp.domain

object Ucid:
  opaque type T <: String = String
  def apply(ucid: String): T = ucid

type Ucid = Ucid.T

object AccountId:
  opaque type T <: String = String
  def apply(accountId: String): T = accountId

type AccountId = AccountId.T

object ClientId:
  opaque type T <: String = String
  def apply(clientId: String): T = clientId

type ClientId = ClientId.T

object CardNumber:
  opaque type T <: String = String
  def apply(cardNumber: String): T = cardNumber

type CardNumber = CardNumber.T

object CardCvv:
  opaque type T <: String = String
  def apply(cardCvv: String): T = cardCvv

type CardCvv = CardCvv.T

object CardExpirationDate:
  opaque type T <: String = String
  def apply(cardExpirationDate: String): T = cardExpirationDate

type CardExpirationDate = CardExpirationDate.T

object PhoneNumber:
  opaque type T <: String = String
  def apply(phoneNumber: String): T = phoneNumber

type PhoneNumber = PhoneNumber.T
