package homework.parket

import homework.validation._

sealed trait HouseType
case object HouseTypePremium extends HouseType
case object HouseTypeEconom extends HouseType

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

  def main(args: Array[String]): Unit = {
    val house: Either[HouseValidation, House] = House(10, 10, 10, 10, HouseTypePremium)
    val house2 = new House(10, 10, 10, 10, HouseTypeEconom)
    println(house2.price)
  }
}