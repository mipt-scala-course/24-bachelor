package mipt

enum ProductionEvent(val factoryName: String):

  // Напиток произведен
  case BeverageProduced(override val factoryName: String, bewerageName: String)
    extends ProductionEvent(factoryName)

  // На заводе произошла одна техническая ошибка
  case BeverageTechnicalError(override val factoryName: String)
    extends ProductionEvent(factoryName)

  // Производится починка оборудования
  case BeverageProductionRepair(override val factoryName: String, repairTime: Long)
    extends ProductionEvent(factoryName)

  // Починка завершена
  case BeverageProductionRepaired(override val factoryName: String)
    extends ProductionEvent(factoryName)

  // Работник Баханов выпил напитки
  case BeverageProductionNeglect(override val factoryName: String)
    extends ProductionEvent(factoryName)

  // Достигнут лимит по времени производства напитков на одной из фабрик
  case BeverageProductionTimeout(override val factoryName: String)
    extends ProductionEvent(factoryName)
