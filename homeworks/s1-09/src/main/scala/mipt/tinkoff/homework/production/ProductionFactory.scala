package mipt.tinkoff.homework
package production

import zio._
import Homeworks._
import ProductionEvent._
import ProductionFailure._

/**
  * Класс фабрики, которая приняв заказ, готова начать его исполнять
  */
trait ProductionFactory {

  def startOrder(
      content: Map[String, Int],
      maxDuration: Duration = Duration.Infinity
  ): ZIO[Any, ProductionFailure, Map[String, Int]]

}

object ProductionFactory {

  case class ProductionFactoryConfig(
      factoryName: String,
      unitProductionMs: Long,
      neglectTreshold: Int,
      neglectProbability: Double,
      repairTreshold: Int,
      repairProbability: Double,
      repairTimeMs: Long
  )

  def apply(
      config: ProductionFactoryConfig,
      eventAggregator: ProductionEventsAggregator
  ): ProductionFactory =
    new Impl(config, eventAggregator)

  private class Impl(
      config: ProductionFactoryConfig,
      eventAggregator: ProductionEventsAggregator
  ) extends ProductionFactory {
    private val neglection        = ZIO.fail(BeverageProductionNeglect(config.factoryName))
    private val brokening         = ZIO.fail(BeverageTechnicalError(config.factoryName))
    private val totallyDestructed = ZIO.fail(ReparingTreshold(config.factoryName))
    private val totallyNeglected  = ZIO.fail(NeglectionTreshold(config.factoryName))

    override def startOrder(
        content: Map[String, Int],
        maxDuration: Duration = Duration.Infinity
    ): ZIO[Any, ProductionFailure, Map[String, Int]] = {

      /**
        * Попытка производства одного экземпляра напитка
        */
      def oneBottle(
          repairCounter: FiberRef[Int],
          neglectCounter: FiberRef[Int]
      )(
          bewerage: String
      ): ZIO[Any, ProductionFailure, String] =
        task"""
         Реализуйте эффект, который производит одну бутылку напитка
           Производство должно занимать config.unitProductionMs.millisecond
           По истечении этого времени надо сгенерировать случайное число [0.0б 1.0]
           Необходимо используя это число проверить, что бутылка выпита с вероятностью config.repairProbability
           Если бутылка выпита рабочим, надо:
             в eventAggregator передать соответствующее событие
             если выпито слишком много бытылок, необходимо прервать выполнение с ошибкой totallyNeglected
             повторить производство бутылки
           Если оборудование сломалось, надо:
             в eventAggregator передать соответствующее событие
             если оборудование ломалось слишком часто, необходимо прервать выполнение с ошибкой totallyDestructed
             в eventAggregator передать событие начала ремонта
             подождать config.repairTimeMs.millisecond
             в eventAggregator передать событие завершения ремонта
             повторить производство бутылки
        """ (2, 1)
        

      /**
        * Окончательная процедура производства одного напитка
        */
      def productionLine(
          repairCounter: FiberRef[Int],
          neglectCounter: FiberRef[Int]
      )(
          bewerageName: String,
          batchSize: Int
      ): ZIO[Any, ProductionFailure, (String, Int)] =
        task"""
         Реализуйте эффект, собирающий производственную линию одного напитка
           необходимо производить по одной бутылке, пока не будет произведено batchSize бутылок
           необходимо вернуть Tuple из имени напитка и количества произведённых бутылок
           каздое успешное производство бутылки должно сопровождаться отправкой соответствующего события в eventAggregator
        """ (2, 2)


      val productionPipeline =
        ZIO.scoped {
          task"""
           Соберите эффект производства заказа
             едля каждого напитка из content надо собрать производственную линию одного напитка
             каждая производственная линия должна начинать работать тогда, когда заканчивает работу предыдущая
             необходимо вернуть результат работы всех производственных линий.
          """ (2, 4)
        }

      val completedProduction =
        task"""
         Соберите полный эффект производства заказа
           если maxDuration - "бесколнечность", то устанавливать таймаут не нужно
           если задано maxDuration, то оно должно использоваться в качестве таймаута с выбрасыванием ошибки ProductionTimeout
           если произошёл таймаут, то в eventAggregator должно быть отправлено соотвтствующее событие
        """ (2, 4)

      completedProduction
    }
  }

}
