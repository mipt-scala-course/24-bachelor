package ru.tinkoff

import scala.util.control.NoStackTrace

object Errors:
  object InitializationError  extends IllegalStateException("Unable to initialize GLFW")  with NoStackTrace
  object WindowCreateError    extends RuntimeException("No window")                       with NoStackTrace