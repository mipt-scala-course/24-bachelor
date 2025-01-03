
# Домашние задания по курсу Scala в бакалавриате МФТИ

Все домашние задания привязаны к семестру и лекции по номеру и находятся в папке [howeworks](homeworks):
## 1 семестр

1. [Введение в Scala. Синтаксис языка. Hello world.](homeworks/s1-01/README.md)
2. [Алгебраические типы данных. Pattern matching. Параметрический полиморфизм.](homeworks/s1-02/README.md)
3. [Коллекции](homeworks/s1-03/README.md)
4. [Ленивые вычисления](homeworks/s1-04/README.md)
5. [Функциональный дизайн. ZIO.](homeworks/s1-05/README.md)
6. [Наследование и подтипирование](homeworks/s1-06/README.md)
7. [Implicits. Typeclasses.](homeworks/s1-07/README.md)
8. [Scala Future](homeworks/s1-08/README.md)
9. [ZIO Fiber](homeworks/s1-09/README.md)
10. [Newtypes](homeworks/s1-10/README.md)

## 2 семестр
1. [Обзор Scala 3. Введение в метапрограммирование](homeworks/s2-01/README.md)
2. [Метапрограммирование](homeworks/s2-02/README.md)
3. [Паттерны функционального программирования. Функтор. FlatMap.](homeworks/s2-03/README.md)
4. [Паттерны функционального программирования. Applicative. Traverse.](homeworks/s2-04/README.md)
5. [Паттерны функционального программирования. Монада. Monad transformer.](homeworks/s2-05/README.md)
6. [Паттерны функционального программирования. Работа с ошибками.](homeworks/s2-06/README.md)
7. [DSL Initial encoding. Отделяем логику высокого уровня от технических деталей.](homeworks/s2-07/README.md)
8. [Tagless Final encoding. Прячем реализацию логики за trait.](homeworks/s2-08/README.md)
9. [Cats-Effect](homeworks/s2-09/README.md)
10. N/A
11. [Тестирование](homeworks/s2-11/README.md)

[Темы к зачету](exam/s2.md)

## Подготовка рабочего места для работы над домашними заданиями

Для работы над домашними заданиями нужно:
* Установить IntelliJ IDEA и подготовить ее для работы со Scala. Это описано в [инструкции по установке IDEA](docs/idea-install/install.md).
* Для работы над домашними заданиями нужно сделать **приватный форк** данного репозитория. Это описано в [инструкции по созданию форка](docs/create-fork/private-fork.md).

Все домашние задания нужно выполнять строго в своем приватном форке, по необходимости обновляя ветку main из основного репозитория (апстрима). 
Домашнее задание должно выполняться в ветке `homeworks/sN-MM`, соответствующей папке в репозитории.
Это нужно, чтобы на CI были запущены тесты именно для этого домашнего задания.
Решение должно сдаваться в виде pull request в ветку main в вашем же форке (не в основном репозитории!)
