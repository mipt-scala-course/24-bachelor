package mipt.homework6

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SchoolClassSpec extends AnyFlatSpec with Matchers {

  "SchoolClass" should "correctly determine School Class type" in {
    """
      |val geniusAndEnlightenedClass: SchoolClass[KnowNothing] =
      |  new SchoolClass(Seq(new Genius))
      |    .accept(Seq(new Enlightened))
      |    .accept(Seq(new Normal))
      |    .accept(Seq(new PoorlyEducated))
      |    .accept(Seq(new KnowSomething))
      |    .accept(Seq(new Aggressive))
      |    .accept(Seq(new KnowNothing))
      |""".stripMargin should compile

    """
      |val geniusAndEnlightenedClass: SchoolClass[KnowSomething] =
      |  new SchoolClass(Nil)
      |    .accept(Seq(new Enlightened))
      |    .accept(Seq(new KnowSomething))
      |""".stripMargin should compile
  }

  "SchoolClass" should "implement compile time checking" in {
    """
      |val geniusAndEnlightenedClass: SchoolClass[Genius] =
      |  new SchoolClass(Seq(new Genius))
      |    .accept(Seq(new KnowNothing))
      |""".stripMargin shouldNot compile
  }
}
