package mipt.tinkoff.demo.p02_functional

object ImperativeTraced {

  var accumulator = 0L

  def factorial(value: Int): Long = {
    var result = 1L
    var current = value
    while (current >= 1) {
      result = result * current
      current = current - 1
    }
    result
  }

  def calculation(values: Seq[Int]) = {
    values.foreach(
      value => {
        val fact = factorial(value)
        println(s" imper fact: $value => $fact")
        accumulator = accumulator + fact
        println(s" imper accm: ${accumulator - fact} => $accumulator")
      }
    )
    accumulator
  }

}

object ImperativeStyleTraced extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(ImperativeTraced.calculation(indications))
}





object FunctionalTraced {

  def factorial(value: Int, accumulator: Long = 1L): Long =
    value match {
      case current if current <= 1 =>
        accumulator
      case current =>
        factorial(current - 1, accumulator * current)
    }

  def calculation(values: Seq[Int], accumulator: Long = 0L): Long =
    values match {
      case Nil =>
        accumulator
      case head :: tail =>
        val fact = factorial(head)
        println(s" funct fact: $head => $fact")
        val res = calculation(tail, accumulator + fact)
        println(s" funct accm: $accumulator + $fact => ${accumulator + fact}")
        res
    }

}

object FunctionalStyleTraced extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(FunctionalTraced.calculation(indications))
}


object BothResultsTraced extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(
    s"""
       | imperative style result: ${ImperativeTraced.calculation(indications)}
       | functional style result: ${FunctionalTraced.calculation(indications)}
       |""".stripMargin
  )
}
