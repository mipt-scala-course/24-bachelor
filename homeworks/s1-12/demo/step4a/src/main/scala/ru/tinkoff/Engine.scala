package ru.tinkoff

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.slf4j.LoggerFactory
import ru.tinkoff.Shaders.Program
import zio.logging.LogFormat
import zio.logging.slf4j.bridge.Slf4jBridge
import zio.{ExitCode, LogLevel, Runtime, URIO, ZIO, ZIOAppDefault}

import scala.util.control.NoStackTrace
import scala.annotation.tailrec

object Engine extends ZIOAppDefault:

  private val InitialWidth  = 800
  private val InitialHeight = 600

  private val commonAppendix =
    LogFormat.space |-| LogFormat.allAnnotations |-| LogFormat.space |-| LogFormat.spans

  private val logFormatColored =
    LogFormat.colored |-| commonAppendix
  private val loggerPlain =
    Runtime.removeDefaultLoggers >>> zio.logging.console(logFormatColored, LogLevel.Trace) >+> Slf4jBridge.initialize

  private val logFormatSad =
    LogFormat.default |-| commonAppendix
  private val loggerJson =
    Runtime.removeDefaultLoggers >>> zio.logging.consoleJson(logFormatSad, LogLevel.Trace) >+> Slf4jBridge.initialize

  private def init =
    for
      _         <- Canvas.initGlFw
      window    <- WindowHandler.createWindow(InitialWidth, InitialHeight)
      _         <- ZIO.logInfo("Configure interface window")
      _         <- window.configureWindow(InitialWidth, InitialHeight)
      _         <- ZIO.logInfo("Configure graphic context")
      _         <- window.initializeContext
      _         <- ZIO.log("Constructing shader program")
      program   <- ZIO.fromEither(Shaders.Program.load("colored"))
      _          = Scene.init()
      mvp        = Scene.mvp(InitialHeight.toFloat/InitialWidth.toFloat)
      _         <- ZIO.log("Prepared to show the window")
      _         <- window.showWindow
    yield (window, program, mvp)


  private def loop(window: WindowHandler, program: Program, mvp: Matrix4f) =
    @tailrec
    def innerLoop: Unit =
      if (window.shouldClose)
        println("closing")
        ()
      else
        drawFrame(program, mvp)
        glfwPollEvents()
        println("next")
        innerLoop
    innerLoop


  private val mvpFloatBuffer = BufferUtils.createFloatBuffer(4 * 4)
  private def drawFrame(program: Program, mvp: Matrix4f) =
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    mvp.get(mvpFloatBuffer)
    glUniformMatrix4fv(
      program.uniform("MVP").uniformId,
      false,
      mvpFloatBuffer
    )
    glUseProgram(program.programId)

    //TODO: drawMesh




  def run: URIO[Any, ExitCode] =
    init
    .map {
      case (window, program, mvp) =>
        loop(window, program, mvp)
    }
    .provide(loggerPlain)
    .exitCode

