package ru.mipt.fp.cache

import scala.concurrent.duration.FiniteDuration

/** Интерфейс универсального кэша
  */
trait Cache[F[_], K, V]:

  def get(key: K): F[Option[V]]
  def put(key: K, value: V): F[Unit]
  def expire(key: K, ttl: FiniteDuration): F[Unit]
  def invalidate(key: K): F[Unit]
