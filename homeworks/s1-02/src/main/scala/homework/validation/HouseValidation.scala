package homework.validation

sealed trait HouseValidation {
  def errorMessage: String
}

case object HouseSizeIsInvalid extends HouseValidation {
  def errorMessage: String = "length, width and height of the house must be positive"
}

case object HouseFloorsAreInvalid extends HouseValidation {
  def errorMessage: String = "floors of the house must be positive"
}
