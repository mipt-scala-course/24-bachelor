# Постановка задачи

## Цель задания

Задание предназначено для закрепления знаний и формирования умений написания DSL в Initial Encoding и использования DSL

## Контекст

Возвращаемся к задаче из прошлого семестра, когда писали на ZIO производственный комбинат с несколькими конкурирующими фабриками по производству напитков. Перед нами стоит задача отделения высокоуровневой логики от деталей реализации. Задача упрощается, фабрика будет только одна и код описывающий работу одного конвейера производства напидков вынесен и оформлен отдельно.

## Постановка задачи

### 1. Производственный конвейер ProductionPipeline

Нарабатываем опыт написания интерпретаторов.

В файле `ProductionPipeline.scala` уже есть ADT, конструкторы и операторы, а так же заготовка Runtime-интерпретатора

Так же в объекте-компаньоне описан синтаксис extension для удобного комбинирования объектов DSL и исполнения.

От вас требуется реализовать Runtime. Давайте посмотрим на его заготовку

```scala
    object Runtime:
      def evaluate( 
        factoryName: String, breakCount: Ref[Int], retryCount: Ref[Int]
      )(
          eventAggregator: ProductionEventsAggregator
      )(
          pipeline: ProductionPipeline[Unit, Unit]
      ): IO[ProductionFailure, Unit] =

        val currentBeverage: Ref[String] = Unsafe.unsafe:
          implicit unsafe => Ref.unsafe.make("")

        def inner(pipeline: ProductionPipeline[?, ?], breaks: Ref[Int], retries: Ref[Int]): IOProductionFailure | PipelineError, Unit] =
          ???

        inner(pipeline, breakCount, retryCount).catchAll:
            case unexpected: PipelineError =>
                ZIO.die(new Exception(s"Unexpected error: ${unexpected.toString()}"))
            case expected: ProductionFailure =>
                ZIO.fail(expected)
```

Функция запуска конвейера `evaluate` принимает на вход общие для всех напитков счёткики поломок и брака, так же локально создаётся `currentBeverage` - хранилище имени текущего производимого напитка. Каждый раз, когда успешно произведён напиток, необходимо отправлять событие `BeverageProduced(factoryName, beverageName)` в `eventAggregator`. Здесь вам currentBeverage и пригодится.

Очевидно, что для полного обхода структуры вам понадобится рекурсия. По этой причине основная часть кода вынесена в отдельный метод `inner`

Каждый раз, когда случается поломка, необходимо:
  - отправить событие `BeverageTechnicalError(factoryName)`
  - проверить первышение Repair Treshold
    - если превышен, то завершить работу конвейера с ошибкой `ProductionFailure.ReparingTreshold(factoryName)`
    - если не превышен, то:
      - отправить событие `BeverageProductionRepair(factoryName, repairingTimeMs)`
      - подождать repairingTimeMs миллисекунд
      - отправить событие `BeverageProductionRepaired(factoryName)`
      - продолжить работу

Каждый раз, когда случается брак надо:
 - отправить событие `BeverageProductionNeglect(factoryName)`
 - запустить производство этой бутылки заново

У типа ProductionPipeline есть два тайп-параметра `BrokenningHandled` и `NeglectionHandled` - это фантомные типы. 
Они существуют для того, что бы вы при построении шаблона вычисления (верхнеуровневая логика), указав что конвейер может сломаться с какой-то вероятностью, не забыли навесит ьобработку поломок, где должны быть счётчики и проверки. аналогично, если на конвейере может произойти брак, то нужна обработка этого события

### 2. Фабрика напитков ProductionFactory

Нарабатываем опыт написания конструкторов и операторов DSL

В файле `ProductionFactory.scala` уже есть ADT и Runtime-интерпретатор, который высокоуровнево описывает производственный конвейер, счётчики брака и полом и запускает последовательно производство всех напитков на этом конвейере

От вас требуется написать:

- конструктор
- операторы
- синтаксис

Заготовки для этих методов уже присутствуют

### 3. Производственная площадка ProductionDomain

Отрабатываем написание высокоуровневой логики используя DSL

В файле `ProductionFactory.scala` описан запуск нашей программы, конфигурация тестовой фабрики.

Вам необходимо построить pipeline - сборку фабрики и отправку заказа ей на исполнение.

Обратите внимение на то, что все методы `ProductionFactory` возвращают `ZIO`. Удобнее всего для комбинирования будет использовать `for-comprehansion`

