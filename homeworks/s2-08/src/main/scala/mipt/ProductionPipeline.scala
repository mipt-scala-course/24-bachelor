package mipt

import zio.{Duration, IO, Ref, Unsafe, ZIO}

trait ProductionPipeline[BrokenningHandled, NeglectionHandled]:
    def setBrokeProbability(probability: Double): ProductionPipeline[Nothing, NeglectionHandled]
    def repairParameters(treshold: Int, repairing: Duration): ProductionPipeline[Unit, NeglectionHandled]
    def setNeglectProbability(probability: Double): ProductionPipeline[BrokenningHandled, Nothing]
    def setRetryParameters(treshold: Int): ProductionPipeline[BrokenningHandled, Unit]
    def totalCount(count: Int): ProductionPipeline[BrokenningHandled, NeglectionHandled]
    def run: IO[ProductionFailure, Unit] 

object ProductionPipeline {
    import ProductionEvent.*

    enum PipelineError:
        case Neglection, Brokening

    // Pipeline construction

    def apply(factoryName: String, beverageName: String, productionMillis: Duration): ProductionPipelinePartiallyApplied =
        new ProductionPipelinePartiallyApplied(factoryName, beverageName, productionMillis)

    class ProductionPipelinePartiallyApplied(factoryName: String, beverageName: String, productionMillis: Duration) {
        def apply(breakCount: Ref[Int], retryCount: Ref[Int], eventAggregator: ProductionEventsAggregator): ProductionPipeline[Unit, Unit] =
            val unit =
                ZIO.logInfo(s"Start to produce one bottle of $beverageName on $factoryName") *>
                ZIO.sleep(productionMillis) *>
                ZIO.unit
            new Impl(factoryName, beverageName, productionMillis, breakCount, retryCount, unit)(eventAggregator)
    }

    // Pipeline instance

    class Impl[BrokenningHandled, NeglectionHandled](
        factoryName: String,
        beverageName: String, 
        productionMillis: Duration,
        breakCount: Ref[Int],
        retryCount: Ref[Int],
        pipeline: IO[PipelineError | ProductionFailure, Unit]
    )(
        implicit eventAggregator: ProductionEventsAggregator
    ) extends ProductionPipeline[BrokenningHandled, NeglectionHandled] {

        override def setBrokeProbability(probability: Double): ProductionPipeline[Nothing, NeglectionHandled] =
            ???

        override def repairParameters(treshold: Int, repairing: Duration): ProductionPipeline[Unit, NeglectionHandled] =
            ???

        override def setNeglectProbability(probability: Double): ProductionPipeline[BrokenningHandled, Nothing] =
            ???

        override def setRetryParameters(treshold: Int): ProductionPipeline[BrokenningHandled, Unit] =
            ???

        override def totalCount(productCount: Int): ProductionPipeline[BrokenningHandled, NeglectionHandled] =
            ???

        override def run: IO[ProductionFailure, Unit] =
            pipeline.catchAll:
                case e: PipelineError =>
                    ZIO.die(new Exception(e.toString()))
                case f: ProductionFailure =>
                    ZIO.fail(f)

    }

}
