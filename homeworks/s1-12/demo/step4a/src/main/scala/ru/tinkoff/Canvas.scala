package ru.tinkoff

import org.joml.Matrix4f
import org.slf4j.LoggerFactory
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.*
import ru.tinkoff.Shaders.Program
import zio.{ZIO, ZIOAspect}

import scala.util.Try
import scala.util.control.NoStackTrace


object Canvas:
  import Errors.*
  private val slf4jLogger = LoggerFactory.getLogger("Canvas")

  def initGlFw: ZIO[Any, Throwable, Unit] =
    slf4jLogger.debug("Initializing GLFW")
    glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))
    ZIO.fail(InitializationError)
      .when(!glfwInit()).unit


