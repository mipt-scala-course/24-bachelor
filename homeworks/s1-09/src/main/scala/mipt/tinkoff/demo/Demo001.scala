package mipt.tinkoff.demo

import java.util.concurrent.Executors
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.Success

object Demo001 extends App {

  private val threadPool                    = Executors.newCachedThreadPool()
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(threadPool)

  // -- *** --- //

  trait SocialService {
    def getUserName(userId: Long): Future[String]
  }

  // -- *** --- //

  val socialService =
    new SocialService {
      override def getUserName(userId: Long): Future[String] =
        Future {
          println(f"$userId%7d: Thread started, \t${Thread.currentThread().getName}%s")
          Thread.sleep(1000L)
          println(f"$userId%7d: Thread finished, \t${Thread.currentThread().getName}%s")
          s"User Userovich ${userId}ov"
        }
//      {
//        val p = Promise[String]
//        new Thread (
//          new Runnable {
//            override def run(): Unit = {
//              println(f"$userId%7d: Thread started, \t${Thread.currentThread().getName}%s")
//              Thread.sleep(1000L)
//              println(f"$userId%7d: Thread finished, \t${Thread.currentThread().getName}%s")
//              p.complete(Success(s"User Userovich ${userId}ov"))
//            }
//          }
//        ).start()
//        p.future
//      }

    }

  // -- *** --- //

  val startedAt = System.currentTimeMillis()

  val resultsF = Future.traverse((1 to 1_000_000).toList) { userId => socialService.getUserName(userId) }

  println("Waiting for completion")

  val result = Await.result(resultsF, Duration.Inf)

  val finishedAt = System.currentTimeMillis()

  println(result.take(100))
  println(finishedAt - startedAt)

  // -- *** --- //

  threadPool.shutdown()
  if (!threadPool.isTerminated)
    threadPool.shutdownNow()

}
