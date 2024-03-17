package mipt.tinkoff.demo.p04_control

import scala.util.Try


case class Division(quotient: Int, reminder: Int)



object DirtyDemo extends App {


  def devide(dividend: Int, divisor: Int): Division =
    Division(dividend / divisor, dividend % divisor)



  println(devide( 21,  7))
  println(devide( 17, 13))
  println(devide(123, 19))
  println(devide(123,  0))
  println(devide(123, 31))


}


object CleanDemo extends App {


  def devide(dividend: Int, divisor: Int): Try[Division] =
    Try {
      Division(dividend / divisor, dividend % divisor)
    }


  println(devide(21, 7))
  println(devide(17, 13))
  println(devide(123, 19))
  println(devide(123,  0))
  println(devide(123, 31))


}

