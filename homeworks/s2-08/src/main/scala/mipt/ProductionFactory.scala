package mipt

import zio.{Duration, Ref, IO, ZIO}
import zio.durationLong

enum ProductionFactoryInitError:
    case NotConfigured, NotOrdered

type FactoryIO[A] = IO[ProductionFactoryInitError | ProductionFailure, A]

trait ProductionFactory:
    def configure(factoryConfig: ProductionFactoryConfig): FactoryIO[ProductionFactory]
    def order(order: Map[ProductionItem, Int]): FactoryIO[ProductionFactory]
    def produce(maxDuration: Duration, eventAggregator: ProductionEventsAggregator): FactoryIO[Map[ProductionItem, Int]]

object ProductionFactory:
    import ProductionFactoryInitError.*

    def make(): FactoryIO[ProductionFactory] =
        ZIO.succeed(new Initial())

    private class Initial() extends ProductionFactory:

        override def configure(factoryConfig: ProductionFactoryConfig): FactoryIO[ProductionFactory] =
            ???

        override def order(order: Map[ProductionItem, Int]): FactoryIO[ProductionFactory] =
            ???

        override def produce(maxDuration: Duration, eventAggregator: ProductionEventsAggregator): FactoryIO[Map[ProductionItem, Int]] =
            ???

    private class Configured(factoryConfig: ProductionFactoryConfig) extends ProductionFactory:

        override def configure(factoryConfig: ProductionFactoryConfig): FactoryIO[ProductionFactory] =
            ???

        override def order(order: Map[ProductionItem, Int]): FactoryIO[ProductionFactory] =
            ???

        override def produce(maxDuration: Duration, eventAggregator: ProductionEventsAggregator): FactoryIO[Map[ProductionItem, Int]] =
            ???

    private class Ordered(
        factoryConfig: ProductionFactoryConfig, 
        order: Map[ProductionItem, Int]
    ) extends ProductionFactory:

        override def configure(factoryConfig: ProductionFactoryConfig): FactoryIO[ProductionFactory] =
            ???

        override def order(order: Map[ProductionItem, Int]): FactoryIO[ProductionFactory] =
            ???

        override def produce(maxDuration: Duration, eventAggregator: ProductionEventsAggregator): FactoryIO[Map[ProductionItem, Int]] =
            val factoryName    = factoryConfig.factoryName
            val factoryLatency = factoryConfig.unitProductionMs.millis

            def preparePipelines(breakCount: Ref[Int], retryCount: Ref[Int], eventAggregator: ProductionEventsAggregator) =
                order.map:
                    (beverage, count) =>
                        ProductionPipeline(factoryName, beverage.value, factoryLatency)(breakCount, retryCount, eventAggregator)
                            .setBrokeProbability(factoryConfig.repairProbability)
                            .repairParameters(factoryConfig.repairTreshold, factoryConfig.repairTimeMs.millis)
                            .setNeglectProbability(factoryConfig.neglectProbability)
                            .setRetryParameters(factoryConfig.neglectTreshold)
                            .totalCount(count)

            for
                breakCount <- Ref.make(0)
                retryCount <- Ref.make(0)
                pipelines   = preparePipelines(breakCount, retryCount, eventAggregator)
                pipeline   <- ZIO
                    .foreach(pipelines)(_.run)
                    .timeoutFail(ProductionFailure.ProductionTimeout(factoryName))(maxDuration)
            yield order
        end produce
