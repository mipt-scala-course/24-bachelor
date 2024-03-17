package mipt.tinkoff.homework
package learnzio

import Homeworks._
import zio._
import java.io.IOException

/**
 * Задание на написание консольной игры с битвой между героем и чудовищем.
 * Для решения понадобится вспомнить Service Pattern, воспользоваться zio.Random,
 * аналогично тому как на лекции использовался zio.Console,
 * пригодятся контрукторы и операторы ZIO
 */
object HeroGame extends ZIOAppDefault {
  import HeroGameUtil._

  /**
   * Оружие героя.
   * Обладает свойствами: максимально наносимый одним ударом урон, число ударов за одну атаку.
   */
  trait Weapon {
    def speed: Int
    def maxDamage: Int
  }

  object Weapon {
    private case class Default(speed: Int, maxDamage: Int) extends Weapon

    lazy val tinSword         : Weapon = Default(2, 3)
    lazy val trainingStiletto : Weapon = Default(1, 1)
  }


  /**
   * Враждебное чудовище, моб
   */
  trait Mob {
    def hp: Int // Показатель здоровья моба
    def to: Int // время на подготовку к удару

    /**
     * Метод проверки хода героя.
     * Должен проверить оставшийся уровень HP и оставшееся число ходов героя.
     *
     * @param damage - урон, наносимый мобу за одну атаку
     * @return - эффект с новым состоянием моба или окончанием битвы
     */
    def prepare(damage: Int): ZIO[Any, MobException, Mob]
  }

  /**
   * Допишите реализацию сервиса враждебного моба выполнив задание task(3, 1)
   */
  object Mob {

    private case class Default(hp: Int, to: Int) extends Mob {
      override def prepare(damage: Int): ZIO[Any, MobException, Mob] =
        task"""
            Реализуйте проверку хода с точки зрения моба:
              Если нанесённый урон больше оставшегося здоровья, то ход заканчивается с MobIsDead
              Если это последний ход (to == 1), то ход заканчивается с MobAttacs
              В ином случае возвращаем новое состояние моба (уменьшаем зоровье и время на подготовку)
        """(3, 1)
    }

    lazy val slimOgre : Mob = Default(14, 3)
    lazy val mannequin: Mob = Default(3, 14)
    object IronStatue extends Mob { self =>
      override val hp: Int = Int.MaxValue
      override val to: Int = 0
      override def prepare(damage: Int): ZIO[Any, MobException, Mob] =
        ZIO.fail(MobAttacs)
    }
  }


  /**
   * Наш храбрый герой
   */
  trait Hero {
    /**
     * Метод, описывающий совершения героем одной атаки (ход героя)
     * Вычисляет урон за одну атаку по формуле: (уронОдногоУдара * weapon.speed)
     * Урон одного удара вычияляется в пределах: [1 .. weapon.maxDamage]
     *
     * @param foe - Противник, на которого должна ыть совершена одна атака
     * @return - новое состояние моба после соверения атаки
     */
    def attack(foe: Mob): ZIO[Any, MobException, Mob]
  }

  /**
   * Допишите реализацию сервиса враждебного моба выполнив задание task(3, 2)
   */
  object Hero {
    def make(sword: Weapon): Hero =
      task"""
          реализуйте функцию создания героя такого, который мог бы атаковать выбранным оружием указанного противника
            Подсчитайте общий урон с помощью zio.Random для заданного оружия (случайный допустимый урон * скорость)
            Выведите в консоль строку вида "Hero deals <здесь подсчитанный урон> damage"
            Нанесите подсчитанный урон противнику и получите новое состояние
      """(3, 2)
  }


  /**
   * Механизм проведения сражения.
   */
  trait Fighting {
    /**
     * функция проведения боя.
     * Эффект, проводящий серию раундов(ходов, атак героя). До тех пор, пока монстр не атакует героя или не погибнет.
     * Сражение всегда заканчивается или MobIsDead или MobAttacs в E-канале
     *
     * @return эффект, с вычислением результата боя
     */
    def fight: ZIO[Any, MobException, Unit]
  }

  /**
   * Напишите игровую механику для битвы героя с чудовищем, решив задание task(3, 3)
   */
  object Fighting {
    def make(hero: Hero, foe: Mob): Fighting =
       new Fighting {
         override def fight: ZIO[Any, MobException, Unit] = {
           task"""
               Опишите алгоритм сражения такой, который будет атаковать полученным героем указанного противника
                 пока не наступит победа одного из них
               Сделайте так, что бы в конце боя осуществлялся вывод результата в консоль: "You win!" или "You die!"

               Например:
                   Hero deals 2 damage
                   Hero deals 4 damage
                   Hero deals 6 damage
                   You win!

               Или
                   Hero deals 2 damage
                   Hero deals 2 damage
                   Hero deals 4 damage
                   You die!
               """(3, 3)

         }

       }

  }

  /////////////////////////////////
  // Игра

  /**
   * Соберите слои так, что бы получилась игра, выполнив задание task(3, 4)
   */
  val game =
    ZIO
      .serviceWithZIO[Fighting](_.fight)
      .provide(
        task"""
              Соберите слои так, что бы получилась игра, в которой бы
              сражался Герой с жестяным мечом(tin sword) против тощего огра (slim ogre)
              """(3, 4)
      )

  /**
   * Запустите несколько раз для проверки того, что шанс на победу есть у каждой из сторон
   */
  override def run =
    game.catchAll(_ => ZIO.unit)

}
