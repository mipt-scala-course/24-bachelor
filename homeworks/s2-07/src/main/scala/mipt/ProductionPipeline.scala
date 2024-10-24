package mipt

import zio.{Duration, IO, Ref, Unsafe, ZIO}


enum ProductionPipeline[BrokenningHandled, NeglectionHandled]:
    case Production       (beverage: ProductionItem,    productionTime: Duration)    extends ProductionPipeline[Unit, Unit]
    case ProdCount[B, N]  (pipeline: ProductionPipeline[B, N], productCount: Int)    extends ProductionPipeline[B, N]

    case Brokeable[N]      (pipeline: ProductionPipeline[?, N], probability: Double) extends ProductionPipeline[Nothing, N]
    case Neglectable[B]    (pipeline: ProductionPipeline[B, ?], probability: Double) extends ProductionPipeline[B, Nothing]

    case Repaired[N] (
        brokable: ProductionPipeline[Nothing, N],
        treshold: Int,
        repairTime: Duration
    ) extends ProductionPipeline[Unit, N]
    case Reworked[B] (
        neglectable: ProductionPipeline[B, Nothing],
        treshold: Int
    ) extends ProductionPipeline[B, Unit]


object ProductionPipeline:
    import ProductionEvent.*

    enum PipelineError:
        case Neglection, Brokening

    // Constructor

    def apply(beverage: ProductionItem, productionTime: Duration): ProductionPipeline[Unit, Unit] =
        Production(beverage, productionTime)

    // Operators

    def setBrokeProbability[N](
        pipeline: ProductionPipeline[Unit, N]
    )(
        probability: Double
    ): ProductionPipeline[Nothing, N] =
        Brokeable[N](pipeline, probability)

    def setBrokeningHandlingTreshold[N](
        pipeline: ProductionPipeline[Nothing, N]
    )(
        treshold:  Int,
        repairing: Duration
    ): ProductionPipeline[Unit, N] =
        Repaired[N](pipeline, treshold, repairing)

    def setNeglectProbability[B](
        pipeline: ProductionPipeline[B, Unit]
    )(
        probability: Double
    ): ProductionPipeline[B, Nothing] =
        Neglectable[B](pipeline, probability)

    def setNeclectionHandlingTreshold[B](
        pipeline: ProductionPipeline[B, Nothing]
    )(
        treshold: Int
    ): ProductionPipeline[B, Unit] =
        Reworked[B](pipeline, treshold)
    
    def setIterationCount(
        pipeline: ProductionPipeline[Unit, Unit]
    )(
        count: Int
    ): ProductionPipeline[Unit, Unit] =
        ProdCount(pipeline, count)



    // Interpreter

    object Runtime:
        def evaluate(
            factoryName: String,
            breakCount: Ref[Int],
            retryCount: Ref[Int]
        )(
            eventAggregator: ProductionEventsAggregator
        )(
            pipeline: ProductionPipeline[Unit, Unit]
        ): IO[ProductionFailure, Unit] =
            val currentBeverage: Ref[String] = Unsafe.unsafe:
                 implicit unsafe => Ref.unsafe.make("")

            def inner(pipeline: ProductionPipeline[?, ?], breaks: Ref[Int], retries: Ref[Int]): IO[ProductionFailure | PipelineError, Unit] =
                pipeline match

                    /**
                     * "Чистое" производство одной бутылки продукта
                     */
                    case Production(bottleName, productionTime) =>
                        ???

                    /**
                     * Сформированый пайплайн должен быть выполнен productCount раз итого.
                     * Успешное завершение пайплайна должно отправлять событие BeverageProduced в eventAggregator
                     */
                    case ProdCount(pipeline, productCount) =>
                        ???

                    /**
                     * Возможность конвейера ломаться.
                     * Вероятность поломки вычисляется как "значение между [0.001, 1.0) <= probability"
                     * Поломка приводит к тому, что результат будет ZIO.fail(PipelineError.Brokening)
                     */
                    case Brokeable(toBeBrokable, probability) =>
                        ???

                    /**
                     * Возможность производства бракованного напитка.
                     * Вероятность брака вычисляется как "значение между [0.001, 1.0) <= probability"
                     * Брак приводит к тому, что результат будет ZIO.fail(PipelineError.Neglection)
                     */
                    case Neglectable(toBeNeglectable, probability) =>
                        ???

                    /**
                     * Если конвейер может ломаться, то должен быть механизм реагирования на поломку
                     * Надо определять ситуации, когда конвейер сломан (PipelineError.Brokening) и:
                     * - инкрементировать счётчик поломок
                     * - проверять счетчик поломок на превышение treshold
                     * - отправлять событие BeverageProductionRepair(factoryName, repairing)
                     * - ждать завершения ремонта в течение repairing
                     * - отправлять событие BeverageProductionRepaired(factoryName)
                     */
                    case Repaired(brokable, treshold, repairing) =>
                        ???

                    /**
                     * Если конвейер может выпускать брак, то должен быть механизм реагирования
                     * Надо определять ситуации, когда кнапиток бракованный (PipelineError.Neglection) и:
                     * - инкрементировать счётчик брака
                     * - проверять счетчик брака на превышение treshold
                     * - отправлять событие BeverageProductionNeglect(factoryName)
                     * - повторять пайплайн ещё раз
                     */
                    case Reworked(neglectable, treshold) =>
                        ???


            inner(pipeline, breakCount, retryCount).catchAll:
                case unexpected: PipelineError =>
                    ZIO.die(new Exception(s"Unexpected error: ${unexpected.toString()}"))
                case expected: ProductionFailure =>
                    ZIO.fail(expected)
            

    // Operator and Runime syntax

    extension [N] (pipeline: ProductionPipeline[Unit, N])

        def canBroke(probability: Double): ProductionPipeline[Nothing, N] =
            setBrokeProbability(pipeline)(probability)

    extension [N] (pipeline: ProductionPipeline[Nothing, N])

        def repairParameters(treshold: Int, latency: Duration): ProductionPipeline[Unit, N] =
            setBrokeningHandlingTreshold(pipeline)(treshold, latency)

    extension [B] (pipeline: ProductionPipeline[B, Unit])

        def canNeglect(probability: Double): ProductionPipeline[B, Nothing] =
            setNeglectProbability(pipeline)(probability)

    extension [B] (pipeline: ProductionPipeline[B, Nothing])

        def retryTreshold(treshold: Int): ProductionPipeline[B, Unit] =
            setNeclectionHandlingTreshold(pipeline)(treshold)

    extension (pipeline: ProductionPipeline[Unit, Unit])

        def totalCount(count: Int): ProductionPipeline[Unit, Unit] =
            setIterationCount(pipeline)(count)

        def produce(factoryName: String, eventAggregator: ProductionEventsAggregator, breakCount: Ref[Int], retryCount: Ref[Int]) =
            Runtime.evaluate(factoryName, breakCount, retryCount)(eventAggregator)(pipeline)
    
