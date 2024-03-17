package mipt.tinkoff.demo.p03_lazyness

object CallByValueTest extends App {

  println("Constructing test data")
  val testData = new TestData()
  println("Constructed test data")
  println()

  def callsFor(index: String, animal: TestData.Animal) = {
    println(s"Calls for $index")
    println(s"first call,  ${animal.name} => ${animal.age}")
    println(s"second call, ${animal.name} => ${animal.age}")
    println(s"third call,  ${animal.name} => ${animal.age}")
    println()
  }


  callsFor("X", testData.x)
  callsFor("Y", testData.y)
  callsFor("Z", testData.z)

}















// ---


object CallByNameTest extends App {

  println("Constructing test data")
  val testData = new TestData()
  println("Constructed test data")
  println()

  def callsFor(index: String, animal: => TestData.Animal) = {
    println(s"Calls for $index")
    println(s"first call,  ${animal.name} => ${animal.age}")
    println(s"second call, ${animal.name} => ${animal.age}")
    println(s"third call,  ${animal.name} => ${animal.age}")
    println()
  }


  callsFor("X", testData.x)
  callsFor("Y", testData.y)
  callsFor("Z", testData.z)

}















// ---



object SmartDemo extends App {
  import TestData._
  val testData = new TestData()

  def naivePrintAnimalAge(animal: Option[Animal], default: String): Unit =
    animal match {
      case Some(value)  =>
        println(s"> Animal named '${value.name}' of age ${value.age}")
      case None         =>
        println(s"> Nobody of age $default")
    }

  def smartPrintAnimalAge(animal: Option[Animal], default: => String): Unit =
    animal match {
      case Some(value)  =>
        println(s"> Animal named '${value.name}' of age ${value.age}")
      case None         =>
        println(s"> Nobody of age $default")
    }

  def defaultAgeString(age: Int): String = {
    println(s"> preparing default age string, $age")
    s"Default age is $age"
  }




  naivePrintAnimalAge(Some(testData.y), defaultAgeString(1))
  naivePrintAnimalAge(None,             defaultAgeString(2))

  smartPrintAnimalAge(Some(testData.x), defaultAgeString(3))
  smartPrintAnimalAge(None,             defaultAgeString(4))

}
