package mipt.tinkoff.homework.production

sealed trait ProductionEvent {
  def factoryName: String
}

object ProductionEvent {

  // Напиток произведен
  final case class BeverageProduced(factoryName: String, bewerageName: String) extends ProductionEvent

  // На заводе произошла одна техническая ошибка
  final case class BeverageTechnicalError(factoryName: String) extends ProductionEvent

  // Производится починка оборудования
  final case class BeverageProductionRepair(factoryName: String, repairTime: Long) extends ProductionEvent

  // Починка завершена
  final case class BeverageProductionRepaired(factoryName: String) extends ProductionEvent

  // Работник Баханов выпил напитки
  final case class BeverageProductionNeglect(factoryName: String) extends ProductionEvent

  // Достигнут лимит по времени производства напитков на одной из фабрик
  final case class BeverageProductionTimeout(factoryName: String) extends ProductionEvent
}
