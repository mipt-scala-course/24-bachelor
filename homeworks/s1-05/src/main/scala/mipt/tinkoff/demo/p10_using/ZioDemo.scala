package mipt.tinkoff.demo.p10_using

import zio._

import java.io.{BufferedReader, FileReader}

object ZioDemo extends ZIOAppDefault {


  def openFile(fileName: String): Task[BufferedReader] =
    ZIO.attemptBlocking(new BufferedReader(new FileReader(fileName)))

  def closeFile(fileReader: BufferedReader): UIO[Unit] =
    ZIO.attemptBlocking(fileReader.close()).catchAll(_ => ZIO.unit)

  def readFile(fileReader: BufferedReader): Task[String] =
    ZIO.attemptBlocking(fileReader.readLine())


  // ---


  val resource1: Task[String] =
    ZIO.acquireReleaseWith(
      openFile("file1.txt")
    )(
      file => closeFile(file)
    )(
      file => readFile(file)
    )


//  override def run =
//    resource1.flatMap(string => Console.printLine(string))

  // ---



  val resource2: Task[(String, String)] =
    ZIO.acquireReleaseWith(
      openFile("file1.txt")
    )(
      file => closeFile(file)
    )(
      file1 =>
        ZIO.acquireReleaseWith(
          openFile("file2.txt")
        )(
          file => closeFile(file)
        )(
          file2 => readFile(file1) <*> readFile(file2)
        )
    )


//  override def run =
//      resource2.flatMap(string => Console.printLine(string))




  // ---



  val acquire = ZIO.acquireReleaseWith(openFile("file1.txt"))
  val release = acquire(file => closeFile(file))
  val usage   = release(file => readFile(file))










  // Introducing Scope

  trait MyScope {
    def addFinalizer(finalizer: Exit[Any, Any] => UIO[Any]): UIO[Unit]

    def close(exit: Exit[Any, Any]): UIO[Any]
  }




  object MyScope {
    val make: UIO[MyScope] = ???
  }




  def fileResource(fileName: String): ZIO[MyScope, Throwable, BufferedReader] =
    ???





  // ---
  //
  // ZIO Scope
  //
  // ---

  def fileResourceImpl(fileName: String): ZIO[Scope, Throwable, BufferedReader] =
    ZIO.acquireRelease(openFile(fileName))(file => closeFile(file))


  lazy val fileReaderLifecicle: ZIO[Any, Throwable, String] =
     ZIO.scoped {
    for {
      file1     <- fileResourceImpl("file1.txt")
      scope     <- ZIO.service[Scope]
      _         <- scope.addFinalizer(zio.Console.printLine("finalize this").catchAll(_ => ZIO.unit))
      file2     <- fileResourceImpl("file2.txt")
      content1  <- readFile(file1)
      content2  <- readFile(file2)
    } yield s"<[$content1], [$content2]>"
   }


  override def run =
    fileReaderLifecicle.flatMap(string => Console.printLine(string))


// --

  val closable: ZIO[Scope, Throwable, BufferedReader] =
    ZIO.fromAutoCloseable(openFile("file1.txt"))


}


