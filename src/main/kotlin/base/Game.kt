package base

import base.gamestates.Level
import glm_.vec2.Vec2i
import glm_.vec3.Vec3ui
import gln.BlendFactor
import gln.State
import gln.gl
import helper.Timer
import org.lwjgl.system.CallbackI
import uno.glfw.GLFWErrorCallbackT
import uno.glfw.GlfwWindow
import uno.glfw.VSync
import uno.glfw.glfw
import java.awt.Color

object Game {

    val window:GlfwWindow

    private const val FPS=120
   private const val UPS=30

    val timer=Timer()

    private var running=false

    const val title= "BreakoutOpenGl"
    val resolution=Vec2i(1200, 800)

    var currentLevelPosition:Int=0
    private set

    //TODO: Normally the Main Menu as first
    private var currentState:GameStateBase

    private var totalLevels:Int=0

    private val background=Vec3ui(33, 33, 33) //RGB


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


        gl.enable(State.BLEND)
        gl.blendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)

        currentState=Level(window)
    }


    fun start(){
        running=true
        window.show()
        drawLoop()
    }

    fun end(){
        currentState.end()
        window.destroy()
        glfw.terminate()
    }


    private fun drawLoop(){

        var delta=0.0
        var accumulator = 0.0
        val interval:Double= 1.0 / UPS
        var alpha=0.0

        window.loop {

            /* Get delta time and update the accumulator */
            delta = timer.delta;

            currentState.handleInput()
            /* Update game and timer UPS if enough time has passed */
           currentState.update(delta)

            /* Render game and update timer FPS */
            currentState.render()
            timer.updateFPS();

            /* Update timer */
            timer.update();

            if(glfw.swapInterval==VSync.ON)
                sync()
        }

    }


    fun createAndStartNewLevel(window: GlfwWindow){
        if(currentState is Level){
            if(!(currentState as Level).finished)
                //TODO: Maybe an Error?
                return
        }
    }

    private fun sync() {
        val lastLoopTime = timer.lastLoopTime
        var now = timer.time
        val targetTime = 1f / FPS
        while (now - lastLoopTime < targetTime) {
            Thread.yield()

            /* This is optional if you want your game to stop consuming too much
             * CPU but you will loose some accuracy because Thread.sleep(1)
             * could sleep longer than 1 millisecond */try {
                Thread.sleep(1)
            } catch (ex: InterruptedException) {
              println(ex.toString())
            }

            now = timer.time
        }
    }

}