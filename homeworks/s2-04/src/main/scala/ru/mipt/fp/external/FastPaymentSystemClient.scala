package ru.mipt.fp.external

import ru.mipt.fp.domain.{FastPaymentsSystemCustomer, PhoneNumber}

/** Интерфейс к системе быстрых платежей
  */
trait FastPaymentSystemClient[F[_]]:
  def searchCustomersByPhoneNumber(phoneNumber: String): F[List[FastPaymentsSystemCustomer]]
