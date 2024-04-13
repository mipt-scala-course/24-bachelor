package mipt.tinkoff.homework.production

sealed abstract class ProductionItem(val value: String)

object ProductionItem {
  case object Sayany  extends ProductionItem("Sparkling Water Саяны (Франция)")
  case object Kvas    extends ProductionItem("Квас 'Лягаевский'")
  case object Compot  extends ProductionItem("Калинские компоты")
  case object RosTish extends ProductionItem("Напитки для детей 'РосТишкин' (Россия)")
}
