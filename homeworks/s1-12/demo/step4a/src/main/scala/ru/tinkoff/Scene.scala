package ru.tinkoff

import org.joml.{Math, Matrix4f, Vector3f, Vector3fc}
import org.lwjgl.opengl.GL11.*
import org.slf4j.LoggerFactory

object Scene:
  private val slf4jLogger = LoggerFactory.getLogger("Scene")

  private val ViewAngle = Math.toRadians(45.0f)

  private val LeftLimit   = -10.0f
  private val RightLimit  =  10.0f
  private val TopLimit    = -10.0f
  private val BottomLimit =  10.0f

  private val NearZ = 0.01f
  private val FarZ = 100.0f

  private def viewMatrix(
    eyePos:     Vector3fc = new Vector3f(4.0f, 3.0f, 3.0f),
    targetPos:  Vector3fc = new Vector3f(0.0f, 0.0f, 0.0f),
    upDir:      Vector3fc = new Vector3f(0.0f, 1.0f, 0.0f)
  ) =
    new Matrix4f()
      .lookAt( eyePos, targetPos, upDir )

  def init(): Unit =
    glClearColor(0.7, 0.7, 0.7, 0.0)
    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LESS)

  def mvp(aspectRatio: Float, isOrtho: Boolean = false): Matrix4f =
    if (isOrtho)
      new Matrix4f()
        .ortho(LeftLimit, RightLimit, TopLimit*aspectRatio, BottomLimit*aspectRatio, NearZ, FarZ)
        .mul(viewMatrix())
    else
      new Matrix4f()
        .perspective(ViewAngle, aspectRatio, NearZ, FarZ)
        .mul(viewMatrix())

