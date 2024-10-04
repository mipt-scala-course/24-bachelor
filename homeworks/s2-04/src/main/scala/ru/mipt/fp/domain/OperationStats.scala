package ru.mipt.fp.domain

import java.time.Instant
import scala.concurrent.duration.FiniteDuration

case class CardOperationStats(cardNumber: CardNumber, income: Double, outcome: Double)

case class OperationStats(
    income: Double,
    outcome: Double,
    cardOperations: List[CardOperationStats]
)
