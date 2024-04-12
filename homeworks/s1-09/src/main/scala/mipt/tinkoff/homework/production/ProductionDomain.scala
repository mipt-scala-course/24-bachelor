package mipt.tinkoff.homework
package production

import zio._
import Homeworks._
import ProductionItem._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object ProductionDomain extends ZIOAppDefault {

  // Конструкторы фабрик
  private def makeIdealFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Ideal factory", 4L, 0, 0.0, Int.MaxValue, 0.0, 1), ea)

  private def makeBrokableFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Brokable factory", 3L, 0, 0.0, 5, 0.8, 1), ea)

  private def makeNeglectableFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(
      ProductionFactory.ProductionFactoryConfig("Neglectable factory", 3L, 7, 0.5, Int.MaxValue, 0.0, 1),
      ea
    )

  private val simpleSource =
    Map(Sayany.value -> 1, Kvas.value -> 2, Compot.value -> 3, RosTish.value -> 5)

  /**
    * Производство напитков по одному заказу с заданным(или нет) таймаутом производства
    *
    * @param order - заказ на производство, список напитков с указанием необходимого количества каждого напитка
    * @param eventAggregator - приёмник сообщений, генерируемых в процессе производства
    * @param timeout - таймаут. Может быть не задан, тогда будет использован "без ограничений"
    * @return
    */
  def production(
      order: Map[String, Int],
      eventAggregator: ProductionEventsAggregator,
      timeout: Duration = Duration.Infinity
  ): ZIO[Any, Nothing, Unit] =
    task"""
        Реализуйте эффект, который запускает три разных фабрики используя заказ simpleSource
        Работа фабрик должна осуществляться параллельно. Используйте файберы.
        Завершение производства на одной из фабрик должно останавливать производство на всех остальных фабриках.
        Результат работы каждой фабрики должен логироваться с уровнем INFO для успеха и ERROR для провала так, что бы было понятно, что за фабрика достигла успеха или какая ошибка произошла
    """ (1, 1)
    

  override def run: ZIO[Scope, Any, Unit] =
    for {
      ea     <- ProductionEventsAggregator.makeAggregator
      result <- production(simpleSource, ea, 255.millisecond)
      _      <- ZIO.logInfo("Messages are:")
      events <- ea.getAllEvents()
      _      <- ZIO.foreachDiscard(events)(evt => ZIO.logInfo(evt.toString))
    } yield result

  override val bootstrap: ZLayer[Any, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)

}
