# Постановка задачи

Это задание предназначено для усвоения материала лекции, формирования навыков написания кода в функциональном стиле и навыков работы с базовыми конструкциями ZIO. Необходимо выполнить три задания:

* Функциональный эффект в декларативной кодировке: [mipt/tinkoff/homework/declarative/TIO.scala](src/main/scala/mipt/tinkoff/homework/declarative/TIO.scala)
* Фнекциональный эффект в исполняемой кодировке: [mipt/tinkoff/homework/executable/TIO.scala](src/main/scala/mipt/tinkoff/homework/executable/TIO.scala)
* Использование ZIO и ZLayer для моделировани битвы Героя с Монстром: [mipt/tinkoff/homework/learnzio/HeroGame.scala](src/main/scala/mipt/tinkoff/homework/learnzio/HeroGame.scala)


# Задачи


## Функциональный эффект в декларативной кодировке

Решение задачи даст вам опыт написания кода в декларативной кодировке.

У вас есть эффект, позволяющий строить вычисления, но он не позволяет в случае ошики восстановиться и продолжить вычисления.
* Необходимо написать конструктор для эффекта с ошибкой
* Необходимо написать оператор для восстановления из состояния с ошибкой
* Необходимо доработать интерпретатор так, что бы он обрабатывать случай восстановления из ошибки


## Функциональный эффект в исполняемой кодировке

Решение задачи даст вам опыт написания кода в исполняемой кодировке.

У вас есть эффект, позволяющий строить вычисления, но он не позволяет в случае ошики восстановиться и продолжить вычисления.
* Необходимо написать конструктор для эффекта с ошибкой
* Необходимо написать оператор для восстановления из состояния с ошибкой


## ZIO и ZLayer

Цель задачи: получить опыт написания кода с помощью ZIO, освоить DI и Service Pattern для разбиения кода на сервисы, получить опыт работы с ZLayer

### Снаряжение/оружие героя

В коде приведён пример trait и реализации, а так же два экземпляра оружия, которым может быть снаряжён герой.
Код приведён в качестве примера и не требует доработки


### Враждебное чудище/Моб

Чудище замахивается на героя своей огромной дубиной и убивает с одного удара.

Чудище очень медлительное и Герой может успеть провести несколько атак.

После каждой атаки Чудище проверяет, сколько здоровья у него осталось и как долго еще будет замахиваться. При этом может совершить атаку (если время замаха вышло), может умереть (если здоровье кончилось).

В коде приведён код реализации сервиса Чудища, но в нем не хватает тела метода проверки атаки Героя. Требуется написать его так, что бы осуществлялась описанная выше проверка.


### Храбрый Герой

Герой очень быстрый и, в зависимости от параметров оружия, может наносить несколько ударов за одну атаку.

Каждая атака героя может или убить Чудище, или ранить его или закончиться атакой Чудища.

В объекте-компаньоне уже есть конструктор для создания Героя. Конструктор учитывает, каким оружием должен быть снаряжён Герой. Требуется написать реализацию этого метода.


### Эпическая Битва

Сервис проведения боёвв должен начинать атаки Героя на Чудище и обрабытыать ситуацию завершения боя.

В объекте-компаньоне уже есть конструктор для Битвы, вам необходимо написать его реализацию.


### Игра

В конечном итоге вам надо собрать готовый эффект, предоставив необходимые сервисы методом .provide


В объектах-компаньонах сервисов лежат готовые объекты и кнструкторы, из которых можно получить жестяной меч (tin sword) и тощего огра (slim ogre)

Битва героя, снаряжённого таким оружием, против такого противника должна завершаться то победой одной стороны, то другой.

Запустите несколько раз, убедитесь, что это действительно так. Попробуйте собрать слои с разными комбинациями снаряжения и монстров.

Добавьте в набор слоёв ZLayer.Debug.tree и ZLayer.Debug.mermaid, посмотрите на диаграму зависимости между вашими слоями.

