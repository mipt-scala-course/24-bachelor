package mipt

import zio.*
import zio.test.*
import zio.test.Assertion.*

import ProductionItem.*
import ProductionEvent.*
import ProductionFailure.*

object ProductionFactorySpec extends ZIOSpecDefault:
    import TestData.*

    val test1 =
        test("produce all goods for ideal condifions") {
                for {
                    aggregator <- ProductionEventsAggregator.makeAggregator
                    initial    <- ProductionFactory.make()
                    configured <- initial.configure(IdealConfig)
                    ordered    <- configured.order(order)
                    production <- ordered.produce(7_000.millis, aggregator).fork
                    _          <- TestClock.adjust(10.second)
                    result     <- production.join
                    events     <- aggregator.getAllEvents()
                } yield
                    assert(result == order)(isTrue) &&
                    assert(events)(isNonEmpty) &&
                    assert(events.filter(_.isInstanceOf[BeverageProduced]).size == order.size)(isTrue)
            }

    val test2 =
        test("fail on production timed out") {
                ProductionEventsAggregator.makeAggregator.flatMap { aggregator => (
                    for
                        initial    <- ProductionFactory.make()
                        configured <- initial.configure(SlowConfig)
                        ordered    <- configured.order(order)
                        production <- ordered.produce(3_000.millis, aggregator).fork
                        _          <- TestClock.adjust(10.second)
                        result     <- production.join
                    yield assert(result)(isEmpty))
                    .catchAll( err =>
                        for { events <- aggregator.getAllEvents() } yield
                            assert(err == ProductionTimeout("Slow Factory"))(isTrue) &&
                            assert(events)(contains(BeverageProductionTimeout("Slow Factory")))
                    )
                }
            }

    val test3 =
        test("fail on repair treshold reached") {
            ProductionEventsAggregator.makeAggregator.flatMap { aggregator => (
                for
                    initial    <- ProductionFactory.make()
                    configured <- initial.configure(BrokableConfig)
                    ordered    <- configured.order(order)
                    production <- ordered.produce(7_000.millis, aggregator).fork
                    _          <- TestClock.adjust(10.second)
                    result     <- production.join
                yield assert(result)(isEmpty))
                .catchAll( err =>
                for (events <- aggregator.getAllEvents())
                    yield assert(err == ReparingTreshold("Brokable Factory"))(isTrue) &&
                    assert(events)(contains(BeverageTechnicalError("Brokable Factory"))) &&
                    assert(events)(contains(BeverageProductionRepair("Brokable Factory", 100L)))
                )
            }
        }


    override def spec: Spec[TestEnvironment & Scope, Any] =
        suite("Factory must")(
            test1, test2, test3
        )
    end spec



object TestData:

    val order =
        Map(Sayany -> 1, Kvas -> 1, Compot -> 1, RosTish -> 1)

    val IdealConfig =
            ProductionFactoryConfig(
                factoryName         = "Ideal Factory",
                unitProductionMs    = 100L,
                neglectTreshold     = 0,
                neglectProbability  = 0.0,
                repairTreshold      = 0,
                repairProbability   = 0.0,
                repairTimeMs        = 0L
            )

    val BrokableConfig =
            ProductionFactoryConfig(
                factoryName         = "Brokable Factory",
                unitProductionMs    = 100L,
                neglectTreshold     = 0,
                neglectProbability  = 0.0,
                repairTreshold      = 3,
                repairProbability   = 1.0,
                repairTimeMs        = 100L
            ) 

    val NeglectableConfig =
            ProductionFactoryConfig(
                factoryName         = "Neglectable Factory",
                unitProductionMs    = 100L,
                neglectTreshold     = 3,
                neglectProbability  = 1.0,
                repairTreshold      = 0,
                repairProbability   = 0.0,
                repairTimeMs        = 0L
            )

    val SlowConfig =
            ProductionFactoryConfig(
                factoryName         = "Slow Factory",
                unitProductionMs    = 1000L,
                neglectTreshold     = 0,
                neglectProbability  = 0.0,
                repairTreshold      = 0,
                repairProbability   = 0.0,
                repairTimeMs        = 0L
            )

