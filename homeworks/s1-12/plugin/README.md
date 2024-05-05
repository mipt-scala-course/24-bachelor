# Плагин, считающий число строк в файлах

## постановка задачи

Необходимо в файле [CountingPlugin.scala](src/main/scala/tinkoff/mipt/CountingPlugin.scala):

* Сделать плагин автоматически, безусловно вклюлченным(Triggered)
* Реализовать `countingSettings: Seq[Def.Setting[_]]` для countSourceFiles и countSourceLines так, что бы
    * Первый Setting считал число файлов исходников, а второй считал число строк в одном из файлов
    * Второй ожидал имя вайла в качестве аргумента. 
      * Если аргумент не передан, должен быть вывод сообщения с уровнем логирования error, 
      * если файл с переданным имененм не найден - сообщение с уровнем warn, 
      * если файл найден - число строк в файле с уровнем info
      * если аргументов несколько - берем первый, а остальные игнорируем

## публикация плагина
В любом режиме (интерактивном или пакетном) должна выполниться команда publishLocal
* Если компиляция и сборка не будут успешными, то публикация не произойдёт
* Если успешно произошла публикация, то повторная не сможет выполниться, пока вы не удалите опубликованный артефакты
  * Куда именно публикуются артефакты вы можете увидеть в выводе успешной публикации
  * Это должно быть: `[домашний каталог пользователя]/.ivy2/local/tinkoff.mipt/counting-plugin/scala_2.12/sbt_1.0/1.12.0` (если вы не меняли настройку publishMavenStyle)



## оценка результата

Примеры вывода в проекте использующем плагин:
```
[IJ]countSourceFiles
[info] Counting source files...
[info] 2 files found
[info] Counting source files... done
```


```
[IJ]countSourceLines
[error] Usage: countSourceLines [file name]
```


```
[IJ]countSourceLines Hello
[warn] File named [Hello] is not found
```


```
[IJ]countSourceLines StaticPoems.scala
[info] StaticPoems.scala contains 63 lines
```

