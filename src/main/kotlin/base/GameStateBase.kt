package base

import helper.Renderer
import uno.glfw.GlfwWindow

abstract class GameStateBase(window:GlfwWindow){

   abstract val renderer:Renderer

    abstract fun render()

}