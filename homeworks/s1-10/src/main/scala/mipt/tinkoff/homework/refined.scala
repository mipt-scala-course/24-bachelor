package mipt.tinkoff.homework

object refined {

  /**
  Информацию по библиотеке refined можно найти здесь - https://github.com/fthomas/refined
   **/
  /**
   *   C помощью библиотеки 'refined' создать трейт RefinedNewType:
   *   1. Он должен иметь один тайп параметр - наш фактический тип, например A
   *   2. Он должен иметь тайп мембер - абстрактный тип Valid
   *   3. Он должен иметь тайп мембер - алиас Type выраженный через Refined, A, Valid
   *   4. Он должен иметь реализованный метод apply, принимающий значение фактического типа
   *      и возвращающий Either[String, Type]. Метод должен быть final.
   *
   */
  trait RefinedNewType

  /**
   *   C помощью библиотеки 'refined' cоздать трейт RefinedCollectionNewType:
   *   1. Он должен иметь один тайп параметр - наш фактический тип коллекции, например C
   *   2. Он должен иметь тайп мембер - абстрактный тип Valid
   *   3. Он должен иметь тайп мембер - алиас Type выраженный через Refined, C, Valid
   *   4. Он должен иметь реализованный метод apply, принимающий значение фактического типа
   *      и возвращающий Either[String, Type]. Метод должен быть final.
   *
   */
  trait RefinedCollectionNewType

  /**
   *  С помощью RefinedNewType, RefinedCollectionNewType и библиотеки refined реализовать следующие типы
   */

  /* Ранг может принимать целочисленные значения от 0 до 100 включительно */
  type Rank

  /* OptionalTrue принимает 2 значения, либо None, либо Some(true) */
  type OptionalTrue

  /* Непустой список */
  type NonEmptyList[A]

  /* Числовое множество, содержащее в себе 0 */
  type SetWithZero[A]

  /* IPv4 или IPv6 адрес */
  type IpAddress

  /* Российский мобильный номер (Для простоты - начинается с +79) */
  type RussianMobilePhone

  case class Complex(r: Double, i: Double)

  /* Вывести 0 из типа Complex */
  type Zero

  /* Вывести тип мнимой части из типа Complex */
  type Imaginary

  /* Вывести тип действительной части из типа Complex */
  type Real
}
