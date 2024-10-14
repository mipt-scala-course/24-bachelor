package ru.mipt.fp.service

import java.time.Instant

import cats.Monad
import cats.data.{OptionT, ReaderT}
import cats.syntax.all.*

import ru.mipt.fp.dao.{CardServiceDao, Config}
import ru.mipt.fp.domain.{Card, CardCvv, CardExpirationDate, CardNumber, Ucid}

/** Сервис представляет собой обёртку для обращения к базе данных карт наших клиентов dao - Dao-класс, непосредственно
  * обращающийся к базе данных Обращения в нём написаны низкоуровнево в императивном стиле, наша задача - переписать их
  * так, чтобы было удобно работать с ними в функциональном стиле, в котором выполнена остальная часть проекта
  */
class CardService[F[_]: Monad](dao: CardServiceDao):
  /** Функция получения данных о карте по её Ucid Здесь и во всех функциях ниже передачу конфига и работу с
    * опциональностью значения следует реализовать при помощи трансформеров монад
    */
  def getCardByUcid(ucid: Ucid): ReaderT[OptionT[F, *], Config, Card] = ???

  /** Функция записи новой карты в базу данных с переданными значениями
    */
  def createCard(
      ucid: Ucid,
      number: CardNumber,
      cvv: CardCvv,
      expirationDate: CardExpirationDate
  ): ReaderT[OptionT[F, *], Config, Unit] = ???

  /** Функция обновления значений существующей карты
    */
  def updateCard(
      ucid: Ucid,
      newNumber: Option[CardNumber],
      newCvv: Option[CardCvv],
      newExpirationDate: Option[CardExpirationDate]
  ): ReaderT[OptionT[F, *], Config, Card] = ???

  /** Функция удаления карты из базы
    */
  def deleteCard(ucid: Ucid): ReaderT[OptionT[F, *], Config, Unit] = ???
