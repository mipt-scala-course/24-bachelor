package ru.mipt.fp.external

import ru.mipt.fp.domain.{Customer, PhoneNumber}

/** Интерфейс к мастер-системе данных клиентов.
  */
trait CustomerMasterSystemClient[F[_]]:
  def seachClientsByPhoneNumber(phoneNumber: PhoneNumber): F[List[Customer]]
