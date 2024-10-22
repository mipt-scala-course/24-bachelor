package ru.mipt.fp.domain

object Ucid:
  opaque type T <: String = String
  def apply(ucid: String): T = ucid

type Ucid = Ucid.T

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
