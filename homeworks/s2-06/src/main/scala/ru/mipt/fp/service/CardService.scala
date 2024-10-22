package ru.mipt.fp.service

import cats.{Monad, MonadError}
import cats.data.{OptionT, ReaderT}
import cats.mtl.Ask
import cats.syntax.all.*
import ru.mipt.fp.dao.{CardServiceDao, DaoConfig}
import ru.mipt.fp.dao.CardServiceDao.{ConnectionTimedOut, DaoError}
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}
import ru.mipt.fp.service.CardService.Config
import ru.mipt.fp.util.{Random, Retry, Timer}
import ru.mipt.fp.util.Retry.*

/** Сервис представляет собой обёртку для обращения к базе данных карт наших клиентов dao - Dao-класс, непосредственно
  * обращающийся к базе данных Обращения в нём написаны низкоуровнево в императивном стиле, наша задача - переписать их
  * так, чтобы было удобно работать с ними в функциональном стиле, в котором выполнена остальная часть проекта
  */
class CardService[F[_]: Random: Timer](dao: CardServiceDao)(using
    Ask[F, Config],
    MonadError[F, DaoError],
    Retry[F, DaoError]
):
  /** Функция получения данных о карте по её Ucid Здесь и во всех функциях ниже передачу конфига и работу с
    * опциональностью значения следует реализовать при помощи трансформеров монад
    */
  def getCardByUcid(ucid: Ucid): F[Card] = ???

  /** Функция записи новой карты в базу данных с переданными значениями
    */
  def createCard(
      ucid: Ucid,
      number: CardNumber,
      cvv: CardCvv,
      expirationDate: CardExpirationDate
  ): F[Unit] = ???

  /** Функция обновления значений существующей карты
    */
  def updateCard(
      ucid: Ucid,
      newNumber: Option[CardNumber],
      newCvv: Option[CardCvv],
      newExpirationDate: Option[CardExpirationDate]
  ): F[Card] = ???

  /** Функция удаления карты из базы
    */
  def deleteCard(ucid: Ucid): F[Unit] = ???

object CardService:
  case class ServiceConfig(retryCount: Int, minDelay: Long, maxDelay: Long)
  case class Config(serviceConfig: ServiceConfig, daoConfig: DaoConfig)
