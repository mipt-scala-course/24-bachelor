package homework

class Test extends munit.FunSuite:


  case class Z(double: Double)

  given Eq[Z] = (_, _) => true

  enum A:
    case B(bool: Boolean)
    case C(int: Int, string: String)
    case D(baz: Z, bool: Boolean)


  test("check B equality"):
    given Eq[A.B] = Derivation.deriveEqForProduct[A.B]
    assertEquals(A.B(true) =:= A.B(false), false)
    assertEquals(A.B(false) =:= A.B(false), true)
    assertEquals(A.B(true) =:= A.B(true), true)


  test("check C equality"):
    given Eq[A.C] = Derivation.deriveEqForProduct[A.C]
    assertEquals(A.C(1, "") =:= A.C(1, ""), true)
    assertEquals(A.C(2, "") =:= A.C(2, "foo"), false)
    assertEquals(A.C(1, "foo") =:= A.C(1, "foo"), true)


  test("check D equality"):
    given Eq[A.D] = Derivation.deriveEqForProduct[A.D]
    assertEquals(A.D(Z(1), true) =:= A.D(Z(2), true), true)
    assertEquals(A.D(Z(1), true) =:= A.D(Z(2), false), false)

  println(show(Derivation.deriveEqForSum[A]))
  
  test("check A equality"):
    given Eq[A] = Derivation.deriveEqForSum[A]
    assertEquals(A.D(Z(1), true) =:= A.B(false), false)
    assertEquals(A.D(Z(1), true) =:= A.D(Z(2), true), true)








