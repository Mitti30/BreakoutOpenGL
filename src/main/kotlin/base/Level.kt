package base

import uno.glfw.GlfwWindow
import java.awt.Font
import kotlin.properties.Delegates

//no level without a game
class Level {

    var finished=false
    private set

    var maximumPoints by Delegates.notNull<Int>()
    private set

    var currentPoints:Int=0
    private set

    val currentBalls=3  //


    fun render(window:GlfwWindow){
    }

}