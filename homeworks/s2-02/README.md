# Постановка задачи

Задание предназначено для формирования базовых навыков метапрограммирования, используя низкоуровневый API 

1. Реализовать деривацию для тайпкласса используя Quotes Reflect API [Eq](src/main/scala/homework/Eq.scala)

То есть, имея на входе иерархию:
```scala 3
sealed trait X derives Eq

case class Bar(bool: Boolean) extends X
case class Foo(int: Int, string: String) extends X
```

Макрос должен генерировать примерно такой код:

```scala 3
// Derivation.deriveEqForProduct[Foo]
new Eq[Foo]:
  def eqv(first: Foo, second: Foo): Boolean =
    given_Eq_Int.eqv(first.int, second.int).&&(given_Eq_String.eqv(first.string, second.string))
    
```
и

```scala 3
// Derivation.deriveEqForSum[X]

{
  val eq_Bar = new Eq[Bar]:
    def eqv(first: Bar, second: Bar): Boolean =
      given_Eq_Boolean.eqv(first.bool, second.bool)

  val eq_Foo = new Eq[Foo]:
    def eqv(first: Foo, second: Foo): Boolean =
      given_Eq_Int.eqv(first.int, second.int).&&(given_Eq_String.eqv(first.string, second.string))


  new Eq[X]:
    def eqv(first: X, second: X): Boolean = Tuple2.apply[X, X](first, second) match
      case Tuple2(first: Bar, second: Bar) =>
        eq_Bar.eqv(`first₂`, `second₂`)

      case Tuple2(first: Foo, second: Foo) =>
        eq_Foo.eqv(`first₃`, `second₃`)

      case _ =>
        false
}
```