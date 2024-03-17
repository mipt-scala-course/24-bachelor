package mipt.tinkoff.demo.p05_effect

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









object EffectDemo extends App {

  case class Effect[A](run: () => A) {
    def map[B](f: A => B): Effect[B] =
      Effect(() => f(run()))

    def flatMap[B](f: A => Effect[B]): Effect[B] =
      Effect(() => f(run()).run())

    def zip[B](other: Effect[B]): Effect[(A, B)] =
      Effect(() => run() -> other.run())

  }


  // -- USAGE --

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


  // -- MODIFICATION --


  def calcSizeFunc(value: String): Int = {
    println("> string length calculation")
    value.length
  }

  println("effect modifying")
  val modified = effect.map(calcSizeFunc)
  println("and executing")
  println(modified.run())
  println()



  // -- CHAINING --


  def next: Int => Effect[Double] =
    src => {
      println("> second effect construction")
      Effect(() => src.toDouble / 7)
    }

  println("combine effects")
  val pipeline =
    modified.flatMap(value => next(value))
  println("and executing")
  println(pipeline.run())
  println()


}



object SugarDemo extends App {

  // Effect class

  case class Effect[A](run: () => A) {
    def map[B](f: A => B): Effect[B] =
      Effect(() => f(run()))

    def flatMap[B](f: A => Effect[B]) =
      Effect(() => f(run()).run())

  }

  // Effects

  val first  = Effect(() => "quick brown fox")
  val second = Effect(() => "lazy dog")

  // combine

  val sugared =
    for {
      partOne <- first
      partTwo <- second
    } yield s"$partOne jumps over the $partTwo"

  // run

  println(sugared.run())

}


