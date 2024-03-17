package mipt.tinkoff.demo.p03_lazyness

import scala.annotation.tailrec


object TailRecDemo extends App {

  val listOfInts = 1 :: 2 :: 3 :: 4 :: 5 :: 3 :: 2 :: 1 :: 0 :: 4 :: 5 :: 6 :: Nil

  @tailrec
  def sum(intValues: Seq[Int], acc: Int = 0): Int =
    intValues match {
      case seq if seq.isEmpty => acc
      case seq                => sum(seq.tail, acc + seq.head)
    }

  println(sum(listOfInts))

}


















// ---


object CoRecDemo extends App {

  @tailrec
  def gen(current: Int, stop: Int = 10, acc: List[Int] = Nil): Seq[Int] =
    if (stop < current)
      acc.reverse
    else
      gen(current + 1, stop, current :: acc)

  println(gen(7))

  // Из исходного значения разворачиваем
  // потенциально бесконечную структуру

}

