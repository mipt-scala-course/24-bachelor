package ru.mipt.fp.util

trait Timer[F[_]]:
  def sleep(ms: Long): F[Unit]

object Timer:
  def apply[F[_]](using timer: Timer[F]): Timer[F] = timer
