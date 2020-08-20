package base

import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec3.Vec3ui
import uno.glfw.GlfwWindow

object Game {
    const val title= "BreakoutOpenGl"
    val resolution=Vec2i(1000,800)

    var currentLevelPosition:Int=0
    private set

    private var currentLevel:Level?=null

    private var totalLevels:Int=0

    private val background=Vec3ui(33,33,33) //RGB

    fun createAndStartNewLevel(window:GlfwWindow){
        if(currentLevel?.finished == false){
            System.err.println("Current Level not Finished")
            return
        }

        currentLevel=Level()
        totalLevels++;
        currentLevel?.render(window)

    }

}