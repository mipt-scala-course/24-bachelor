package mimp

import scala.concurrent.duration.FiniteDuration

import cats.effect.{Resource, GenTemporal, Outcome, IO, IOApp}
import cats.effect.std.{Console, Supervisor}

import cats.syntax.all.*
import cats.effect.syntax.all.*
import scala.concurrent.duration.*

/**
  *
  *  Босс, который умеет делегировать
  *
  */

/**
  * Успешный результат выполнения задачи
  *   valueSize - количество вэлью, которое приносит задача бизнесу
  */
case class TaskSuccess(valueSize: Int)

/**
  * Неуспешный результат выполнения задачи
  *   message - оправдание, которым пользуется разработчик если задача не была сделана
  *
  * ResultError - во время выполнения задачи возникли непреодолимые трудности
  * UnexpectedError - во время выполнения задачи возникли неожидаемые непреодолимые трудности
  * Timeout - дедлайн попрошел, задача перестала быть актуальной
  */
sealed trait TaskError:
  def message: String

object TaskError:
  case class ResultError(message: String) extends TaskError
  case class UnexpectedError(message: String) extends TaskError
  case object Timeout extends TaskError:
    val message = "timeout"

type TaskResult = Either[TaskError, TaskSuccess]

/**
  * Задача
  *   jiraId - идентификатор в джире
  *   logic - сама задача в виде синхронного вычисления
  *   timeout - максимальное время которое можно затратить на задачу
  */
case class Task[F[_]](jiraId: String, logic: F[TaskResult], timeout: FiniteDuration = 5.seconds)

/**
  * Интерфейс для "ожидания" выполнения задачи
  */
trait Await[F[_]]:
  def get: F[TaskResult]

object Await:
  def apply[F[_]](f: F[TaskResult]): Await[F] = new Await[F]:
    override val get: F[TaskResult] = f

/**
  * Необходимо реализовать инстанс Boss, который
  *   - может отдать задачу на выполнение с помощью start
  *      (при этом поток выполнения не должен блокироваться, вызов start не должен дожидаться результатов выполнения задачи)
  *
  *   - задача должна начать выполняться сразу после вызова start
  *   - start возвращает Await[F], вызов get блокирует поток выполнения до тех пор, пока задача не будет выполнена
  *
  *   Помимо того, что задача должна выполняться асинхронно, требуется:
  *     - печатать в консоль информацию о том, когда задача начала выполняться
  *     - печатать в консоль информацию о том, когда задача закончила выполняться / отменилась и тд с результатом
  *         задачи, текстом ошибки
  *     - если задача превышает максимальное время выполнения, необходимо отменять задачу, результат выполнения такой задачи должен быть Timeout
  *     - если во время выполнении задачи произошла неожидаемая ошибка, то результат выполнения должен быть UnexpectedError
  *
  *   То есть, печать информации о процессе выполнения задач должен производиться в консоль в ЛЮБОМ СЛУЧАЕ
  *
  */
trait Boss[F[_]]:
  def start(task: Task[F]): F[Await[F]]

object Boss:
  class Impl[F[_]] extends Boss[F]:
    def start(task: Task[F]): F[Await[F]] =
      ???

  def make[F[_]]: Resource[F, Boss[F]] =
    ???

object Main extends IOApp.Simple:
  def program(boss: Boss[IO]): IO[Unit] =
    for
      _ <- boss.start(Task("1", IO.sleep(5.seconds).as(Right(TaskSuccess(0))), 15.seconds))
      _ <- boss.start(Task("2", IO.sleep(3.seconds).as(Right(TaskSuccess(-1))), 2.seconds))
      x <- boss.start(Task("3", IO.sleep(7.seconds).as(Left(TaskError.ResultError("not okey"))), 10.seconds))
      _ <- x.get
      y <- boss.start(Task("4", IO.sleep(2.seconds).as(Right(TaskSuccess(100))), 1.seconds))
      _ <- y.get
    yield ()

  val run: IO[Unit] = {
    Boss.make[IO].flatMap(boss => program(boss).toResource)
  }.use_
