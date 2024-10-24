package mipt

enum ProductionFailure(reason: String):

  case ProductionTimeout(factoryName: String)
    extends ProductionFailure(s"Production time is out on factory '$factoryName'")

  case NeglectionTreshold(factoryName: String)
    extends ProductionFailure(s"Workers is too bad on factory '$factoryName'")

  case ReparingTreshold(factoryName: String)
    extends ProductionFailure(s"Hardware is too bad on factory '$factoryName'")
