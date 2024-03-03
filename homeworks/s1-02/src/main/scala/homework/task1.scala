package homework

import homework.task1.HouseType.{HouseTypeEconom, HouseTypePremium}
import homework.validation.{HouseFloorsAreInvalid, HouseSizeIsInvalid, HouseValidation}

object task1 {
  sealed trait HouseType

  object HouseType {
    case object HouseTypePremium extends HouseType
    case object HouseTypeEconom extends HouseType
  }

  case class House(
                    width: Int,
                    height: Int,
                    length: Int,
                    floors: Int,
                    classification: HouseType
                  ) {
    def price: Double = classification match {
      case HouseTypePremium if floors < 5 => Math.pow(3, floors) * (width + height + length)
      case HouseTypePremium if floors >= 5 => Math.pow(2, floors) * (width + height + length)
      case HouseTypeEconom => width * height * length + floors * 10000
      case _ => 0
    }
  }

  object House {
    def apply(
               width: Int,
               height: Int,
               length: Int,
               floors: Int,
               classification: HouseType
             ): Either[HouseValidation, House] =
      if (width < 0 || height < 0 || length < 0) Left(HouseSizeIsInvalid)
      else if (floors < 0) Left(HouseFloorsAreInvalid)
      else Right(new House(width, height, length, floors, classification))
  }
}
