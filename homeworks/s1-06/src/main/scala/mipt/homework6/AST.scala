package mipt.homework6

import scala.language.dynamics
import scala.language.implicitConversions

/**
 * Есть абстрактное синтаксическое дерево, описывающее язык, состоящий из
 *   - переменных (Ident)
 *   - применения (Apply)
 *   - обращения к полю/методу (Select)
 *   - инфиксная операция (InfixOp)
 *
 *
 * Например, для тестирования абстрактного интрепретатора этого языка, придется
 * составлять термы языка, используя конструкторы выше:
 *
 *   Select(Ident("lol"), "bar")
 *
 *   Apply(
 *     Ident("g"),
 *     Vector(
 *       Apply(
 *         Ident("foo"),
 *         Vector(Ident("arg1"),
 *         Ident("arg2"))
 *       ),
 *       InfixOp(Ident("x"), "boo", Ident("y"))
 *     )
 *   )
 *
 *
 * Задача реализовать более удобный подход, чтобы можно было
 * конструировать эти термы используя соответствующие конструкции самого языка Scala:
 *
 *   "lol".ident.bar
 *
 *   "g".ident(
 *     "foo".ident("arg1".ident, "arg2".ident),
 *     "x".ident  "y".ident
 *   )
 *
 * Для реализации необходимо воспользоваться функциональностью Dynamic с переопределением необходимых методов
 */
sealed trait AST extends Dynamic {
  def apply(arg: AST*): AST = ???
}

case class Ident(name: String) extends AST
case class Apply(t: AST, args: Vector[AST]) extends AST
case class Select(t: AST, field: String) extends AST
case class InfixOp(l: AST, op: String, r: AST) extends AST


object AST {
  /**
   * 2) Реализовать синтаксис .ident строк
   */
  implicit class StringIdentStx(val str: String) extends AnyVal {
    def ident: AST = ???
  }

}
