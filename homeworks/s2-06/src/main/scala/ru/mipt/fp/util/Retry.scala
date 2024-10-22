package ru.mipt.fp.util

import cats.MonadError
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import ru.mipt.fp.service.CardService.ServiceConfig

class Retry[F[_]: Random: Timer, E](using MonadError[F, E]):
  def retry[A](task: => F[A])(predicate: E => Boolean)(config: ServiceConfig): F[A] = ???

object Retry:
  def apply[F[_], E](using retry: Retry[F, E]): Retry[F, E] = retry

  extension [F[_], A](task: => F[A])
    def retry[E](predicate: E => Boolean)(config: ServiceConfig)(using Retry[F, E]): F[A] =
      Retry[F, E].retry(task)(predicate)(config)
