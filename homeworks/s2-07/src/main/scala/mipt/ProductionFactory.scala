package mipt

import zio.{Duration, Ref, IO, ZIO}
import zio.durationLong

enum ProductionFactory:

  case InitialFactory()

  case FactoryConfiguration(
    factory: ProductionFactory,
    factoryConfig: ProductionFactoryConfig
    )

  case ProductionReadyFactory(
    configuredFactory: FactoryConfiguration,
    order: Map[ProductionItem, Int],
    maxDuration: Duration
    )

/**
 * Напишите конструкторы, операторы и extension-ы
 */
object ProductionFactory:

    enum ProductionFactoryInitError:
        case NotConfigured, NotOrdered


    // Constructor

    / **
     * Конструктор должен создавать эффект с пустой ненастроенной фабрикой
     */
    def apply(): IO[ProductionFactoryInitError, ProductionFactory] =
        ZIO.succeed(ProductionFactory.InitialFactory())



    // Operators

    /**
     * - Оператор конфигурирования фабрики для "пустой" фабрики хранит ее и конфигурацию.
     * - Оператор конфигурирования фабрики для "настроенной" фабрики берет ее вложенную фабрику и полученную извне конфигурацию. Фактически меняет конфигурацию, сохраня фабрику.
     * - Оператор конфигурирования фабрики для "фабрики с заказом" берет вложенную фабрику храниммой конфигурированной фабрики и полученную извне конфигурацию. Фактически полнижает уровень фабрики до конфигурированной.
     */
    def configureFactory(
        factory: ProductionFactory
    )(
        factoryConfig: ProductionFactoryConfig
    ): IO[ProductionFactoryInitError, ProductionFactory] =
        ???

    /**
     * Оператор закрепления заказа:
     * - Если фабрика не сконфигурирована, то ответ должен содержать ошибку NotConfigured
     * - Для "настроенной" фабрики должна быть построена "фабрика с заказом"
     * - Для "фабрики с заказом" должна быть построена новая фабрика с заказом используя настроенную фабрику из прежней фабрики с заказом и новый заказ
     */
    def orderFactory(
        factory: ProductionFactory
    )(
        order: Map[ProductionItem, Int],
        maxDuration: Duration
    ): IO[ProductionFactoryInitError, ProductionFactory] =
        ???



    // Runtime / Interpreter

    object Runtime:
        import ProductionPipeline.* 

        def eval(
            factory: ProductionFactory, 
            timeout: Duration, 
            eventAggregator: ProductionEventsAggregator
        ): IO[ProductionFactoryInitError | ProductionFailure, Map[ProductionItem, Int]] =
            factory match
                case InitialFactory() =>
                    ZIO.fail(ProductionFactoryInitError.NotConfigured)
                case FactoryConfiguration(factory, factoryConfig) =>
                    ZIO.fail(ProductionFactoryInitError.NotOrdered)
                case ProductionReadyFactory(configuredFactory, order, maxDuration) =>
                    val config = configuredFactory.factoryConfig
                    val factoryLatency = config.unitProductionMs.millis

                    val prodPipelines  = order.map:
                        (beverage, count) =>
                            ProductionPipeline(beverage, factoryLatency)
                                .canBroke(config.repairProbability)
                                .repairParameters(config.repairTreshold, config.repairTimeMs.millis)
                                .canNeglect(config.neglectProbability)
                                .retryTreshold(config.neglectTreshold)
                                .totalCount(count)

                    for
                        breakCount <- Ref.make(0)
                        retryCount <- Ref.make(0)
                        pipeline   <- ZIO
                                        .foreach(prodPipelines)(_.produce(config.factoryName, eventAggregator, breakCount, retryCount))
                                        .timeoutFail(ProductionFailure.ProductionTimeout(config.factoryName))(timeout)
                    yield order
            


    // Extension syntax for operators & runtime

    extension (factory: ProductionFactory)

        def configure(factoryConfig: ProductionFactoryConfig): IO[ProductionFactoryInitError, ProductionFactory] =
            ???

        def order(order: Map[ProductionItem, Int], maxDuration: Duration = Duration.Infinity): IO[ProductionFactoryInitError, ProductionFactory] =
            ???

        /**
         * Syntax для удобного исполнения заказа с использованием Runtime(интерпретатора по умолчанию)
         * Должен не только вызвыать Runtime.eval, но и отлавливать ошибки так, что бы:
         * - ProductionFactoryInitError должны приводить к ZIO.die(new Exception(s"Unexpected error: ${e.toString()}"))
         * - При ProductionTimeout должно отправляться событие BeverageProductionTimeout в eventAggregator и пробрасываться наружу
         * - Все остальные ProductionFailure должны пробрасываться наружу
         */
        def produce(timeout: Duration, eventAggregator: ProductionEventsAggregator) =
            ???
