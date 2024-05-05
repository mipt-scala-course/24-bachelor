# Проект, использующий плагин

## Подключение плагина
* Плагин уже прописан в файле [counting.sbt](project/counting.sbt):
* Убедитесь, что после того, как в проекте плагина успешно выполнится команда `publishLocal`, этот проект успешно соберётся


## Использование плагина

Плагин добавит два новых таска: `countSourceFiles` и `countSourceLines`

Примеры работы должны быть примерно такими:

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

