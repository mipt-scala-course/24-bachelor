package mipt.tinkoff.demo.p03_lazyness

object TestData {
  case class Animal(name: String, age: Byte) {
    println(s"Registering animal $name of age $age")
  }
}

class TestData {
  import TestData._

  def x       : Animal = Animal("Patrik the cat", 8)
  val y       : Animal = Animal("Iris the guinea pig", 2)
  lazy val z  : Animal = Animal("Cookie the guinea pig", 1)

}










// ---


object InstantiationTest extends App {

  println("Constructing test data")
  val testData = new TestData()
  println("Constructed test data")
  println()

  println("Calls for X")
  println(s"first call,  ${testData.x.name} => ${testData.x.age}")
  println(s"second call, ${testData.x.name} => ${testData.x.age}")
  println(s"third call,  ${testData.x.name} => ${testData.x.age}")
  println()

  println("Calls for Y")
  println(s"first call,  ${testData.y.name} => ${testData.y.age}")
  println(s"second call, ${testData.y.name} => ${testData.y.age}")
  println(s"third call,  ${testData.y.name} => ${testData.y.age}")
  println()

  println("Calls for Z")
  println(s"first call,  ${testData.z.name} => ${testData.z.age}")
  println(s"second call, ${testData.z.name} => ${testData.z.age}")
  println(s"third call,  ${testData.z.name} => ${testData.z.age}")
  println()

}
