package ru.tinkoff

import org.slf4j.LoggerFactory
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.*
import zio.{ZIO, ZIOAspect}

import scala.util.Try
import scala.util.control.NoStackTrace

private val slf4jLogger = LoggerFactory.getLogger("WindowHandler")

opaque type WindowHandler = Long

object WindowHandler:
  import Errors.*


  def createWindow(width: Int, height: Int): ZIO[Any, Throwable, WindowHandler] =
    slf4jLogger.debug(s"Creating window, sized: $width/{}", height)
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    glfwWindowHint(GLFW_SAMPLES, 4)     // 4x anti-aliasing
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3) // OpenGL 3.3
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3) // e.g. #version 330
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    ZIO.fromOption(Option(glfwCreateWindow(width, height, "LWJGL window", NULL, NULL)))
      .orElseFail {
        glfwTerminate()
        //TODO: read glfw error
        WindowCreateError
      }

extension (window: WindowHandler)
  def configureWindow(width: Int, height: Int): ZIO[Any, Throwable, Unit] =
    for
      mode <- ZIO.succeed(glfwGetVideoMode(glfwGetPrimaryMonitor()))
      _ <- ZIO.logDebug("Got video mode") @@ ZIOAspect.annotated("mode", mode.toString)
    yield glfwSetWindowPos(
      window,
      (mode.width() - width) / 2,
      (mode.height() - height) / 2
    )

  def initializeContext: ZIO[Any, Throwable, Unit] =
    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    GL.createCapabilities()
    slf4jLogger.trace("Init completed")
    ZIO.unit

  def shouldClose: Boolean =
    glfwWindowShouldClose(window)

  def showWindow: ZIO[Any, Throwable, Unit] =
  //TODO: read glfw error
    ZIO.fromTry(Try(glfwShowWindow(window)))

  def swapBuffers(): Unit =
    glfwSwapBuffers(window)