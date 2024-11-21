package mipt.service

import mipt.model.{Card, CardNumber}

trait CardMasking:
  def mask(card: Card): Card

object CardMaskingImpl extends CardMasking:

  override def mask(card: Card): Card =
    val lead  = card.number.take(6)
    val trail = card.number.drop(12)
    card.copy(number = CardNumber(s"$lead******$trail"))
