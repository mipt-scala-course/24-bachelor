package mipt.homework6

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServiceLevelAdvanceSpec extends AnyFlatSpec with Matchers {

  "ServiceLevelAdvance" should "correctly determine ServiceLevel type" in {

    """
      |val special1bLevel: ServiceLevelAdvance[Special1b] =
      |  new ServiceLevelAdvance[Economy]
      |    .advance[UpgradedEconomy]
      |    .advance[Special1b]
      |""".stripMargin should compile

    """
      |val specialPlLevel: ServiceLevelAdvance[Platinum] =
      |  new ServiceLevelAdvance[Economy]
      |    .advance[UpgradedEconomy]
      |    .advance
      |""".stripMargin should compile
  }

  "SchoolClass" should "implement compile time checking" in {
    """
      |val eliteLevel =
      |  new ServiceLevelAdvance[Economy]
      |    .advance[Elite]
      |    .advance[Business]
      |""".stripMargin shouldNot compile
  }
}
