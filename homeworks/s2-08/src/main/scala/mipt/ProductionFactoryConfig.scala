package mipt

case class ProductionFactoryConfig(
    factoryName: String,
    unitProductionMs: Long,
    neglectTreshold: Int,
    neglectProbability: Double,
    repairTreshold: Int,
    repairProbability: Double,
    repairTimeMs: Long
)
