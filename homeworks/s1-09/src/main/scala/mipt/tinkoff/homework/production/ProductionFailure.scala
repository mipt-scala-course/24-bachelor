package mipt.tinkoff.homework.production

sealed abstract class ProductionFailure(reason: String) extends Exception(reason)

object ProductionFailure {
  final case class ProductionTimeout(factoryName: String)
      extends ProductionFailure(s"Production time is out on factory '$factoryName'")

  final case class NeglectionTreshold(factoryName: String)
      extends ProductionFailure(s"Workers is too bad on factory '$factoryName'")

  final case class ReparingTreshold(factoryName: String)
      extends ProductionFailure(s"Hardware is too bad on factory '$factoryName'")
}
