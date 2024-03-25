package mipt.homework6

import mipt.homework6.AST.StringIdentStx
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ASTSpec extends AnyFlatSpec with Matchers {

  "AST syntax" should "correctly build AST tree" in {

    "foo".ident.bar shouldBe Select(Ident("foo"), "bar")

    "foo".ident.bar("kek".ident) shouldBe InfixOp(Ident("foo"), "bar", Ident("kek"))

    "foo".ident("arg1".ident, "arg2".ident) shouldBe Apply(Ident("foo"), Vector(Ident("arg1"), Ident("arg2")))

    "g".ident(
      "foo".ident("arg1".ident, "arg2".ident),
      "x".ident boo "y".ident
    ) shouldBe
      Apply(
        Ident("g"),
        Vector(
          Apply(
            Ident("foo"),
            Vector(Ident("arg1"), Ident("arg2"))
          ),
          InfixOp(Ident("x"), "boo", Ident("y"))
        )
      )
  }

}
