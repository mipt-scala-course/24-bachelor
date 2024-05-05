package ru.tinkoff

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.control.NoStackTrace
import scala.util.{Success, Try}

object Shaders:
  opaque type Program = Int
  opaque type Shader  = Int
  opaque type Uniform = Int

  private case class ShaderCompileError(message: String) extends Exception(message) with NoStackTrace
  private case class ProgramLinkingError(message: String) extends Exception(message) with NoStackTrace

  private val slf4jLogger = LoggerFactory.getLogger("Shaders")

  enum ShaderType(postfix: String, val gl: Int):
    case Fragment extends ShaderType("frag", gl = GL_FRAGMENT_SHADER)
    case Vertex   extends ShaderType("vert", gl = GL_VERTEX_SHADER)

    def fileName(name: String) =
      s"$name.$postfix"


  object Shader:
    private def readSource(resName: String) =
      Try(
        Source.fromResource(s"shaders/$resName")
          .getLines()
          .mkString("\n")
      ).transform(
        success =>
          Success(Right(success)),
        failure =>
          slf4jLogger.error("Shader source not loaded because of {}", failure.getMessage)
          Success(Left(failure))
      ).get

    private def compileSource(source: String, shaderType: ShaderType): Either[Throwable, Shader] =
      val shader = glCreateShader(shaderType.gl)
      glShaderSource(shader, source)
      slf4jLogger.debug("Compiling shader {}/{}", shaderType, shaderType.gl)
      glCompileShader(shader)

      glGetShaderi(shader, GL_COMPILE_STATUS) match
        case GL_FALSE =>
          val errorMessage = glGetShaderInfoLog(shader)
          slf4jLogger.error("Shader source not compiled because of {}", errorMessage)
          Left(ShaderCompileError(errorMessage))
        case _ =>
          Right(shader)

    def load(shaderName: String, shaderType: ShaderType): Either[Throwable, Shader] =
      val resName = shaderType.fileName(shaderName)
      for {
        source <- readSource(resName)
        shader <- compileSource(source, shaderType)
      } yield shader


  object Program:
    private def linkProgram(vertexShader: Shader, fragmentShader: Shader): Either[Throwable, Program] =
      val program = glCreateProgram()
      glAttachShader(program, vertexShader)
      glAttachShader(program, fragmentShader)
      slf4jLogger.debug("Linking program {}", program)
      glLinkProgram(program)

      glGetProgrami(program, GL_COMPILE_STATUS) match
        case GL_FALSE =>
          Left(ProgramLinkingError(glGetProgramInfoLog(program)))
        case _ =>
          Right(program)


    def load(programName: String): Either[Throwable, Program] =
      for {
        vertexShader    <- Shader.load(programName, ShaderType.Vertex)
        fragmentShader  <- Shader.load(programName, ShaderType.Fragment)
        program         <- linkProgram(vertexShader, fragmentShader)
      } yield program

  extension(program: Program)
    def uniform(name: String): Uniform =
      glGetUniformLocation(program, name)
    def programId: Int =
      program


  extension(uniform: Uniform)
    def uniformId: Int =
      uniform


