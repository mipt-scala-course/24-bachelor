package ru.mipt.fp.util

trait Random[F[_]]:
  def betweenLong(begin: Long, end: Long): F[Long]

object Random:
  def apply[F[_]](using random: Random[F]): Random[F] = random
