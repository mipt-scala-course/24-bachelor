package homework.task2

import homework.task2.UserProducts.{HeavyProducts, LightProducts}
import homework.task2.UserType._

trait UserSettingsService {
  def getPasswordSettings(userId: UserId): Option[PasswordSettings]
}

object UserSettingsService {
  def apply(userService: UserService): UserSettingsService = new UserSettingsService {

    override def getPasswordSettings(userId: UserId): Option[PasswordSettings] = {
      val passwordSettingsByInfo = userService.getPasswordInfo(userId) match {
        case PasswordInfo(false, true) => Some(PasswordSettings(allowed = true, set = false))
        case PasswordInfo(true, true) => Some(PasswordSettings(allowed = true, set = true))
        case PasswordInfo(false, false) => Some(PasswordSettings(allowed = true, set = true))
        case _ => Some(PasswordSettings(allowed = false, set = false))
      }
      userService.findUser(userId) match {
        case Some(User(_, UserType.Dummy, _)) => Some(PasswordSettings(allowed = false, set = false))
        case Some(User(_, UserType.Customer, _)) => passwordSettingsByInfo
        case Some(User(_, AlmostCustomer, Some(phone))) =>
          userService.findUserProducts(phone) match {
            case Some(LightProducts) =>
              userService.getPasswordInfo(userId) match {
                case PasswordInfo(true, true) => Some(PasswordSettings(allowed = true, set = true))
                case PasswordInfo(false, false) => Some(PasswordSettings(allowed = true, set = true))
                case _ => Some(PasswordSettings(allowed = false, set = false))
              }
            case _ => passwordSettingsByInfo
          }
        case _ => None
      }

    }
  }
}
