package mipt.tinkoff.homework.learnzio

import mipt.tinkoff.homework.learnzio.HeroGame.Mob
import zio._
import zio.test._
import zio.test.Assertion._

object HeroGameSpec extends ZIOSpecDefault {

  private case class DummyHero() extends HeroGame.Hero {

    override def attack(foe: HeroGame.Mob): ZIO[Any, HeroGameUtil.MobException, HeroGame.Mob] =
      foe match {
        case Mob.IronStatue => ZIO.fail(HeroGameUtil.MobAttacs)
        case _              => ZIO.fail(HeroGameUtil.MobIsDead)
      }
  }

  override def spec =
    suite("Hero game test suite")(
      success, failure
    )

  private def success =
    test("Hero wins on Mannequin") {
      val fighting = HeroGame.Fighting.make(DummyHero(), HeroGame.Mob.mannequin)
      fighting.fight
        .fold(
          err => assertTrue(err == HeroGameUtil.MobIsDead),
          _   => assertNever("fight must not working this way")
        )
    }

  private def failure =
    test("Hero fails to Iron Statue") {
      val fighting = HeroGame.Fighting.make(DummyHero(), HeroGame.Mob.IronStatue)
      fighting.fight
        .fold(
          err => assertTrue(err == HeroGameUtil.MobAttacs),
          _   => assertNever("fight must not working this way")
        )
    }

}
