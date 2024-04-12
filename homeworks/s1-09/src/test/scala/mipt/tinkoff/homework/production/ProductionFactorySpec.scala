package mipt.tinkoff.homework.production

import zio._
import zio.test._
import zio.test.Assertion._

import ProductionItem._
import ProductionEvent._
import ProductionFailure._

object ProductionFactorySpec extends ZIOSpecDefault {

  private def makeIdealFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Ideal factory", 1, 0, 0.0, Int.MaxValue, 0.0, 1), ea)

  private def makeTimedFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Timed factory", 1, 0, 0.0, Int.MaxValue, 0.0, 1), ea)

  private def makeBrokableFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Brokable factory", 1, 0, 0.0, 5, 1.0, 1), ea)

  private def makeBrokenTotallyFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(ProductionFactory.ProductionFactoryConfig("Broken totally factory", 1, 0, 0.0, 3, 1.0, 1), ea)

  private def makeNeglectableFactory(ea: ProductionEventsAggregator) =
    ProductionFactory(
      ProductionFactory.ProductionFactoryConfig("Neglectable factory", 1, 5, 0.5, Int.MaxValue, 0.0, 1),
      ea
    )

  private val simpleSource =
    Map(Sayany.value -> 1, Kvas.value -> 1, Compot.value -> 1, RosTish.value -> 1)

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Factory must")(
      test("produce all goods for ideal condifions") {
        for {
          ea     <- ProductionEventsAggregator.makeAggregator
          pf     <- makeIdealFactory(ea).startOrder(simpleSource).fork
          _      <- TestClock.adjust(5.second)
          rl     <- pf.join
          events <- ea.getAllEvents()
        } yield assert(rl == simpleSource)(isTrue) &&
          assert(events)(isNonEmpty) &&
          assert(events)(contains(BeverageProduced("Ideal factory", RosTish.value)))
      },
      test("fail on production timed out") {
        ProductionEventsAggregator.makeAggregator.flatMap { ea =>
          (
            for {
              pf <- makeTimedFactory(ea).startOrder(simpleSource, 2.millisecond).fork
              _  <- TestClock.adjust(5.second)
              rl <- pf.join
            } yield assert(rl)(isEmpty)
          ).catchAll(err =>
            for { events <- ea.getAllEvents() } yield assert(err == ProductionTimeout("Timed factory"))(isTrue) &&
              assert(events)(contains(BeverageProductionTimeout("Timed factory")))
          )
        }
      },
      test("fail on production timed out if repair present") {
        ProductionEventsAggregator.makeAggregator.flatMap {
          ea =>
            (
              for {
                pf <- makeBrokableFactory(ea).startOrder(simpleSource, 7.millisecond).fork
                _  <- TestClock.adjust(5.second)
                rl <- pf.join
              } yield assert(rl)(isEmpty)
            ).catchAll(err =>
              for { events <- ea.getAllEvents() } yield assert(err == ProductionTimeout("Brokable factory"))(isTrue) &&
                assert(events)(contains(BeverageTechnicalError("Brokable factory"))) &&
                assert(events)(contains(BeverageProductionRepair("Brokable factory", 1))) &&
                assert(events)(contains(BeverageProductionRepaired("Brokable factory"))) &&
                assert(events)(contains(BeverageProductionTimeout("Brokable factory")))
            )
        }
      },
      test("fail on production timed out if neglection present") {
        ProductionEventsAggregator.makeAggregator.flatMap {
          ea =>
            (
              for {
                _  <- TestRandom.feedDoubles(0, 0.9, 0, 0.9, 0)
                ea <- ProductionEventsAggregator.makeAggregator
                pf <- makeNeglectableFactory(ea).startOrder(simpleSource, 5.millisecond).fork
                _  <- TestClock.adjust(5.second)
                rl <- pf.join
              } yield assert(rl)(isEmpty)
            ).catchAll(err =>
              for { events <- ea.getAllEvents() } yield assert(events)(isEmpty) &&
                assert(err == ProductionTimeout("Neglectable factory"))(isTrue)
            )
        }
      },
      test("fail on repair treshold reached") {
        ProductionEventsAggregator.makeAggregator.flatMap {
          ea =>
            (
              for {
                pf <- makeBrokenTotallyFactory(ea).startOrder(simpleSource).fork
                _  <- TestClock.adjust(5.second)
                rl <- pf.join
              } yield assert(rl)(isEmpty)
            ).catchAll(err =>
              for (events <- ea.getAllEvents())
                yield assert(err == ReparingTreshold("Broken totally factory"))(isTrue) &&
                  assert(events)(contains(BeverageTechnicalError("Broken totally factory"))) &&
                  assert(events)(contains(BeverageProductionRepair("Broken totally factory", 1)))
            )
        }
      }
    )

}
