package mipt.tinkoff.demo.p03_lazyness

object MyListDemo extends App {

  // Eager List
  trait MyList[+A]
  case class MyCons[A](value: A, tail: MyList[A]) extends MyList[A]
  case object MyTail extends MyList[Nothing]

}























// ---


// Lazy List

object MyLazyListDemo extends App {

  trait MyLazyList[+A] {
    def value: A
    def tail: MyLazyList[A]

    // ---

    def take(n: Int): MyLazyList[A] = {
      def inner(step: Int, tail: MyLazyList[A]): MyLazyList[A] =
        tail match {
          case MyLazyTail                         => MyLazyTail
          case MyLazyCons(_, _)    if n <= step   => MyLazyTail
          case next: MyLazyCons[A] if n == step+1 => MyLazyCons(next.value, () => MyLazyTail)
          case next: MyLazyCons[A]                => MyLazyCons(next.value, () => inner(step+1, next.tail))
        }

      MyLazyCons(value, () => inner(1, tail))
    }

    // ---

    def toList: List[A] = {
      def inner(tail: MyLazyList[A]): List[A] =
        tail match {
          case next: MyLazyCons[A]  => next.value :: inner(next.tail)
          case _                    => Nil
        }

      value :: inner(tail)
    }
  }

  // ---

  case class MyLazyCons[A](value: A, next: () => MyLazyList[A]) extends MyLazyList[A] {
    override lazy val tail = next()
  }

  case object MyLazyTail extends MyLazyList[Nothing] {
    override def value = ???
    override def tail = ???
  }













  // ---

  def nextIntLazyNode(x: Int): MyLazyList[Int] = {
    println(s"Calculate next lazy value for $x")
    if (x > 100)
      MyLazyTail
    else
      MyLazyCons(x, () => nextIntLazyNode(x + 1))
  }

  val potentiallyEndless: MyLazyList[Int] = nextIntLazyNode(0)

  println("lazyness demo:")
  println(potentiallyEndless.value)
  println(potentiallyEndless.tail.tail.value)
  println(potentiallyEndless.tail.tail.value)
  println(potentiallyEndless.tail.tail.tail.value)
  println()

  println("limitation and conversion demo:")
  println(potentiallyEndless.take(7).toList)
  println()

}





















// ---


// Scala LazyList

object LazyListDemo extends App {

  val lazyList: LazyList[Int] = {
    def fibNext(prePrev: Int, prev: Int): LazyList[Int] = {
      val current = prePrev + prev
      current #:: fibNext(prev, current)
    }

    0 #:: 1 #:: fibNext(0, 1)
  }

  println(lazyList.take(10).toList)


}
