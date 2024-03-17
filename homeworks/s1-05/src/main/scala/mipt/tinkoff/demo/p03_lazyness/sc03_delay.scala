package mipt.tinkoff.demo.p03_lazyness

object DelayedFuncDemo extends App {

  val calcVal: () => Int =
    () => 77

  def calcDef(): String =
    "Seventy seven"

  def printCalculated[A](calc: () => A) =
    println(calc())


  printCalculated(calcVal)
  printCalculated(calcDef)

}

















// ---



object WrappedDelayDemo extends App {

  case class Effect[A](run: () => A)


  def newStringFunc() = {
    println("> new string generation")
    "quick brown fox jumps over the lazy dog"
  }

  val effect = {
    println("> effect construction")
    Effect(newStringFunc)
  }
  println()


  println("effect executing")
  println(effect.run())
  println()



}




