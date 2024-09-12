package homework

import Eq.given
import Eq.fields

class Test extends munit.FunSuite:

  test("fields"):
    case class Foo(int: Int, bool: Boolean)

    assertEquals(Foo(2, true).fields, Iterable(2, true))

  test("string equals"):
    assertEquals("Test String" =:= "Test String", true)

  test("string not equals"):
    assertEquals("Test String" =:= "Test String0", false)

  test("different types not compiles"):
    assertEquals(compileErrors(""""Test String" =:= false""").nonEmpty, true)

  test("int equals"):
    assertEquals(0 =:= 0, true)

  test("int not equals"):
    assertEquals(0 =:= 1, false)

  test("boolean equals"):
    assertEquals(false =:= false, true)

  test("boolean not equals"):
    assertEquals(false =:= true, false)

  test("option empty"):
    assertEquals(Option.empty[String] =:= Option.empty[String], true)

  test("option nonempty equals"):
    assertEquals(Some("foo") =:= Some("foo"), true)

  test("option nonempty not equals"):
    assertEquals(Some("foo") =:= Some("foo 2"), false)

  test("different options not compiles"):
    assertEquals(compileErrors("""Some("Test String") =:= Some(2)""").nonEmpty, true)

  test("empty lists"):
    assertEquals(List.empty[String] =:= List.empty[String], true)

  test("non empty lists equals"):
    assertEquals(List("1") =:= List("1"), true)

  test("non empty lists not equals"):
    assertEquals(List("1") =:= List("2"), false)

  test("non empty lists; different types"):
    assertEquals(compileErrors("""List("1") =:= List(1)""").nonEmpty, true)

  test("eqProduct equals"):
    case class Person(name: String, age: Int, sex: Boolean)

    given Eq[Person] = Eq.eqProduct[Person](List(summon[Eq[String]], summon[Eq[Int]], summon[Eq[Boolean]]))

    assertEquals(Person("1", 2, false) =:= Person("1", 2, false), true)


  test("eqProduct not equals"):
    case class Person(name: String, age: Int, sex: Boolean)

    given Eq[Person] = Eq.eqProduct[Person](List(summon[Eq[String]], summon[Eq[Int]], summon[Eq[Boolean]]))

    assertEquals(Person("1", 2, false) =:= Person("2", 2, false), false)

  test("eqSum equals"):
    enum Foo:
      case Bar
      case Baz(x: Int)


    val barEq: Eq[Foo.Bar.type] = Eq.eqProduct(Nil)
    val bazEq: Eq[Foo.Baz] = Eq.eqProduct(List(summon[Eq[Int]]))

    given Eq[Foo] = Eq.eqSum[Foo](List(barEq, bazEq))

    assertEquals((Foo.Baz(1): Foo) =:= (Foo.Baz(1): Foo), true)


  test("eqSum not equals"):
    enum Foo:
      case Bar
      case Baz(x: Int)

    val barEq: Eq[Foo.Bar.type] = Eq.eqProduct(Nil)
    val bazEq: Eq[Foo.Baz] = Eq.eqProduct(List(summon[Eq[Int]]))

    given Eq[Foo] = Eq.eqSum[Foo](List(barEq, bazEq))

    assertEquals((Foo.Baz(1): Foo) =:= (Foo.Bar: Foo), false)

  test("check equals"):
    assertEquals(Eq.check(5, 5, summon[Eq[Int]]), true)


  test("check not equals"):
    assertEquals(Eq.check(5, 6, summon[Eq[Int]]), false)


  test("check fails"):
    intercept[ClassCastException](Eq.check(5, "5", summon[Eq[Int]]))


  test("derived product equals"):
    case class Person(name: String, age: Int, sex: Boolean) derives Eq

    assertEquals(Person("1", 2, false) =:= Person("1", 2, false), true)


  test("derived product not equals"):
    case class Person(name: String, age: Int, sex: Boolean) derives Eq

    assertEquals(Person("1", 2, false) =:= Person("2", 2, false), false)

  test("derived sum equals"):
    enum Foo derives Eq:
      case Bar(y: String)
      case Baz(x: Int)

    assertEquals((Foo.Baz(1): Foo) =:= (Foo.Baz(1): Foo), true)


  test("derived sum not equals"):
    enum Foo derives Eq:
      case Bar(y: String)
      case Baz(x: Int)

    assertEquals((Foo.Baz(1): Foo) =:= (Foo.Bar("1"): Foo), false)

