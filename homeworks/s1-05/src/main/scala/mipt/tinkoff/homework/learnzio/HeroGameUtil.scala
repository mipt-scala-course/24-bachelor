package mipt.tinkoff.homework.learnzio

object HeroGameUtil {



  // Исключительные состояния враждебного моба
  sealed trait MobException
  case object MobIsDead extends MobException
  case object MobAttacs extends MobException
  case object Undefined extends MobException




}
