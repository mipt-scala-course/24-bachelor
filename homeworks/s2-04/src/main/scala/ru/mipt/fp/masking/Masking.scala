package ru.mipt.fp.masking

import cats.{Contravariant, Functor}

/** Тайпкласс для маскирования данных
  */
trait Masking[T]:
  def mask(t: T): T

  extension (t: T) def masked: T = mask(t)
