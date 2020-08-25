package base

import base.gamestates.Level
import glm_.vec2.Vec2i
import glm_.vec3.Vec3ui
import gln.gl
import helper.Timer
import uno.glfw.GLFWErrorCallbackT
import uno.glfw.GlfwWindow
import uno.glfw.VSync
import uno.glfw.glfw
import java.awt.Color

object Game {

    val window:GlfwWindow

    val FPS=60

    val timer=Timer()

    private var running=false

    const val title= "BreakoutOpenGl"
    val resolution=Vec2i(1200,800)

    var currentLevelPosition:Int=0
    private set

    //TODO: Normally the Main Menu as first
    private var currentState:GameStateBase

    private var totalLevels:Int=0

    private val background=Vec3ui(33,33,33) //RGB


    init{

       glfw.init()

        glfw.errorCallback=object : GLFWErrorCallbackT {
            override fun invoke(p1: glfw.Error, p2: String) {
                System.err.println("$p1 , $p2")
            }

        }

        window = GlfwWindow(resolution, title).apply {
            init(false)
            resizable=false}
        glfw.swapInterval=VSync.ON
        gl.clearColor(Color.GRAY)
        gl.viewport(resolution)


        currentState=Level(window)
    }


    fun start(){
        running=true
        window.show()
        drawLoop()
    }


    fun drawLoop(){

        window.loop {

            currentState.render()

        }

    }

    fun createAndStartNewLevel(window:GlfwWindow){
        if(currentState is Level){
            if(!(currentState as Level).finished)
                //TODO: Maybe an Error?
                return
        }
    }

}