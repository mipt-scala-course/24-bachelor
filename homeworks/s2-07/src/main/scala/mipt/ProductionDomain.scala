package mipt

import zio.*
import zio.Console.*


object Main extends ZIOAppDefault:
    import ProductionFactory.*
    import ProductionItem.* 

    override val run =
        val config =
            ProductionFactoryConfig(
                factoryName         = "Test Factory",
                unitProductionMs    = 1000L,
                neglectTreshold     = 10,
                neglectProbability  = 0.25,
                repairTreshold      = 5,
                repairProbability   = 0.10,
                repairTimeMs        = 100L
            )

        val order = Map(
                Sayany  -> 3,
                Kvas    -> 5,
                Compot  -> 2,
                RosTish -> 1
            )

        val ProductionTimeout = 15_000.millis

        /**
         * напишите код, который:
         * - Создаёт ProductionEventsAggregator
         * - Создаёт и инициализтрует ProductionFactory
         * - Отправляет фабрике заказ с таймаутом ProductionTimeout
         * - 
         */
        val pipeline: IO[ProductionFailure, Map[ProductionItem, Int]] =
            ???

        pipeline.flatMap:
            result => printLine(s"Produced: $result")

