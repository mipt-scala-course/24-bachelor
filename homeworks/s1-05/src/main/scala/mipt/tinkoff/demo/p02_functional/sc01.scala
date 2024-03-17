package mipt.tinkoff.demo.p02_functional

object Imperative {
  // change state on each step(line)

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
        accumulator = accumulator + factorial(value)
      }
    )
    accumulator
  }

  // NB:
  // external values/variables
  // mutable structures/variables

}

object ImperativeStyle extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(Imperative.calculation(indications))
}




object Functional {
  // create new state on each state

  @scala.annotation.tailrec
  def factorial(value: Int, accumulator: Long = 1L): Long =
    value match {
      case current if current <= 1 =>
        accumulator
      case current =>
        factorial(current - 1, accumulator * current)
    }

  @scala.annotation.tailrec
  def calculation(values: Seq[Int], accumulator: Long = 0L): Long =
    values match {
      case Nil =>
        accumulator
      case head :: tail =>
        calculation(tail, accumulator + factorial(head))
    }

  // NB:
  // no (or minimal) values
  // immutable values/structures

}

object FunctionalStyle extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(Functional.calculation(indications))
}




object BothResults extends App {
  val indications = 1 :: 2 :: 5 :: 7 :: 4 :: 3 :: 12 :: 5 :: Nil
  println(
    s"""
       | imperative style result: ${Imperative.calculation(indications)}
       | functional style result: ${Functional.calculation(indications)}
       |""".stripMargin
  )
}
