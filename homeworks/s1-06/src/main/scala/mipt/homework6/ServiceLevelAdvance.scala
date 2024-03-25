package mipt.homework6

/**
 * Разрабатываем сервис лояльности/уровней обслуживания для клиентов компании.
 * Уровни лояльности реализованы с помощью иерархии классов, начиная с самого простого Economy (родительский класс для всех остальных)
 * Чем ниже мы продвигаемся по потомкам от Economy тем круче класс.
 * ServiceLevelAdvance необходим для продвижения клиента от менее крутых к более крутым уровням.
 */
class Economy
class UpgradedEconomy extends Economy
class Special1b       extends UpgradedEconomy
class ExtendedEconomy extends Economy
class Business        extends ExtendedEconomy
class Elite           extends Business
class Platinum        extends Business

/**
 * Модифицируйте код таким образом, чтобы ServiceLevelAdvance был привязан к уровню обслуживания,
 * а ServiceLevelAdvance.advance мог только повышать уровень обслуживания.
 * Подсказку, как именно модифицировать, вы найдете в тестах.
 */
class ServiceLevelAdvance {
  def advance: ServiceLevelAdvance = new ServiceLevelAdvance
}
