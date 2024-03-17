package mipt.tinkoff.demo.p07_declarative

/**
 * Демонстрационная реализация декларативного подхода.
 * Не контроллирует ошибки.
 */
object DeclarativeTeleplay {

  // 1. Доменная область
  sealed trait Teleplay {

  }
  case class Act(action: String) extends Teleplay
  case class Say(speech: String) extends Teleplay
  case class Chain(before: Teleplay, after: Teleplay) extends Teleplay
  case class Select(cond: String => Boolean, ifTrue: () => Teleplay, ifFalse: () => Teleplay) extends Teleplay
//  case class Repeat(count: Int, effect: Teleplay) extends Teleplay


  // 2. Конструкторы
  object Teleplay {
    def act(action: String): Teleplay = Act(action)
    def say(speech: String): Teleplay = Say(speech)
  }


  // 3. Операторы
  implicit class TeleplayOps(teleplay: Teleplay) {

    def and(next: Teleplay): Teleplay =
      Chain(teleplay, next)

    def or(cond: String => Boolean, other: => Teleplay): Teleplay =
      Select(cond, () => other, () => teleplay)

    def combined(next: Teleplay, cond: String => Boolean, other: => Teleplay): Teleplay =
      and(next) or (cond, other)

    def repeatN(count: Int): Teleplay = ???

  }


  // 4. Интерпретаторы
  def consolePlay(scenario: Teleplay): Unit =
    scenario match {
      case Act(action) =>
        println(s"* Actor do $action")
      case Say(speech) =>
        println(s"* Actor say: '$speech''")
      case Chain(before, after) => {
        consolePlay(before)
        consolePlay(after)
      }
      case Select(cond, ifTrue, ifFalse) =>
        if (cond(scala.io.StdIn.readLine()))
          consolePlay(ifTrue())
        else
          consolePlay(ifFalse())
    }




  // Другой интерпретатор - другой режим вычислений
  def prettyWrite(scenario: Teleplay): String =
    scenario match {
      case Act(action) =>
        s"Actor do $action"
      case Say(speech) =>
        s"Actor say: '$speech''"
      case Chain(before, after) =>
        s"{ ${prettyWrite(before)} and then ${prettyWrite(after)} }"
      case Select(_, ifTrue, ifFalse) =>
        s"""
           |ask prompter for suggestion
           |if understand => ${prettyWrite(ifTrue())}
           |else => ${prettyWrite(ifFalse())}
           |""".stripMargin
    }

}



object DeclarativeTeleplayDemo extends App {
  import DeclarativeTeleplay._

  // 5. Программа(программы)

  val romeoPlay =
    Teleplay.act("can do nothing")
      .or(
        _ == "Juliet is dead",
        Teleplay.say("Oh, no!") and Teleplay.act("drink poison and... has no more tears")
      )

  val julietPlay: Teleplay =
    Teleplay.act("can do nothing")
      .or(
        _ == "last sleep",
        Teleplay.act("drink special potion") and Teleplay.say("I will try to cheat")
      )
      .or(
        _ == "Romeo is dead",
        Teleplay.say("I have no reason to live") and Teleplay.act("harakiri")
      )


  // 6. Исполнение
  println("Romeo play")
  consolePlay(romeoPlay)

  println("-"*24)

  println("Juliet play")
  println(prettyWrite(julietPlay))

}

