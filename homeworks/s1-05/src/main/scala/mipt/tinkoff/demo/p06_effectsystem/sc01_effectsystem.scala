package mipt.tinkoff.demo.p06_effectsystem

case class Effect[A](runSafe: () => Option[A]) {
  def runUnsafe(): A = runSafe().get

  def map[B](f: A => B): Effect[B] =
    Effect(() =>
      runSafe() match {
        case Some(value) =>
          scala.util.Try(f(value)).toOption
        case fail =>
          fail.asInstanceOf[Option[B]]
      }
    )

  def flatMap[B](f: A => Effect[B]): Effect[B] =
    Effect(() =>
      runSafe() match {
        case Some(value) =>
          f(value).runSafe()
        case failure: None.type =>
          failure
      }
    )

  def foreach(f: A => Unit): Unit =
    runSafe() match {
      case Some(value) =>
        f(value)
      case None =>
        ()
    }

}

object Effect {
  def pure[A](a: A): Effect[A] = Effect(() => Some(a))

  def raise[A]: Effect[A] = Effect(() => None)

}






object EffectSystemDemo extends App {


  val producer = Effect.pure("quick brown fox jumps over the lazy dog")
  def trimmer(string: String) = string.take(16)
  def modifier(string: String) = Effect.pure(string.length)
  def consumer(length: Int) = length.toDouble * 19 / 7



  val calculation =
    producer.map(trimmer).flatMap(modifier).map(consumer)



  println(calculation.runUnsafe())
  calculation.foreach(println)



}






object EffectSystemSugaredDemo extends App {


  val producer = Effect.pure("quick brown fox jumps over the lazy dog")
  def trimmer(string: String) = string.take(16)
  def modifier(string: String) = Effect.pure(string.length)
  def consumer(length: Int) = length.toDouble * 19 / 7



  val calculation =
    for {
      str <- producer
      trm  = trimmer(str)
      mod <- modifier(trm)
    } yield consumer(mod)

  println(calculation.runUnsafe())


  for {
    str <- producer
    trm  = trimmer(str)
    mod <- modifier(trm)
    res  = consumer(mod)
  } println(res)

}



object HelloSystemDemo extends App {


  // Доменная модель
  final case class InOut[A](run: () => A)

  // Конструкторы
  object InOut {
    def ask() = InOut(() => scala.io.StdIn.readLine())
    def say(speech: String) = InOut(() => println(speech))
  }

  // Операторы
  object syntax {
    implicit class InOutOps[A](effect: InOut[A]) {
      def map[B](f: A => B)             = InOut(() => f(effect.run()))
      def flatMap[B](f: A => InOut[B])  = InOut(() => f(effect.run()).run())
      def foreach(f: A => Unit)         = InOut(() => f(effect.run()))
    }
  }
  import syntax._


  // --- Usage --- //

  def ageCheck(age: Long) =
    if (age < 18)
      InOut.say("Too young!")
    else
      InOut.say("Come in!")


  val program =
    for {
      _     <- InOut.say("What is your name?")
      name  <- InOut.ask()
      _     <- InOut.say(s"Hello, $name!")
      _     <- InOut.say("How old are you?")
      age   <- InOut.ask().map(_.toLong)
      _     <- ageCheck(age)
    } yield ()


  program.run()


}
