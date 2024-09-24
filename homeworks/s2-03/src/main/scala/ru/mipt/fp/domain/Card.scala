package ru.mipt.fp.domain

import java.time.Instant
import ru.mipt.fp.masking.Masking

case class Card(ucid: Ucid, number: CardNumber, cvv: CardCvv, expirationDate: CardExpirationDate)
