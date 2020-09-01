package base

import helper.Renderer
import uno.glfw.GlfwWindow

abstract class GameStateBase(window:GlfwWindow){

   abstract val renderer:Renderer

    fun end(){
        renderer.brickTexture.delete()
        renderer.paddleTexture.delete()
        renderer.backgroundTexture.delete()
        renderer.font.texture.delete()
    }

    abstract fun update(delta:Double)

    abstract fun handleInput()

    abstract fun render()

}