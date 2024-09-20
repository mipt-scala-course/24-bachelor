package homework

import scala.quoted.{Expr, Quotes, Type}

inline def show[T](inline value: T): String = ${ showMacro('{value}) }


private def showMacro[T: Type](value: Expr[T])(using quotes: Quotes): Expr[String] =
  import quotes.reflect.*
  Expr(value.asTerm.show(using Printer.TreeShortCode))