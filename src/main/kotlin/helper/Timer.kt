package helper

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetTime
import uno.glfw.Seconds
import uno.glfw.glfw


class Timer {

    /**
     * System time since last loop.
     */
    var lastLoopTime = time
    private set

    /**
     * Used for FPS and UPS calculation.
     */
    private var timeCount = 0f

    /**
     * Frames per second.
     */
    var fps = 0
    get() = if (field > 0) field else fpsCount
    private set

    /**
     * Counter for the FPS calculation.
     */
    private var fpsCount = 0

    /**
     * Updates per second.
     */
    private var ups = 0
    get() = if (field > 0) field else upsCount

    /**
     * Counter for the UPS calculation.
     */
    private var upsCount = 0

    /**
     * Returns the time elapsed since `glfwInit()` in seconds.
     *
     * @return System time in seconds
     */
    val time: Seconds
    get() = glfw.time

    /**
     * Returns the time that have passed since the last loop.
     *
     * @return Delta time in seconds
     */
    val delta: Float
    get(){
        val time = time
        val delta = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        timeCount += delta
        return delta
    }

    /**
     * Updates the FPS counter.
     */
    fun updateFPS() {
        fpsCount++
    }

    /**
     * Updates the UPS counter.
     */
    fun updateUPS() {
        upsCount++
    }

    /**
     * Updates FPS and UPS if a whole second has passed.
     */
    fun update() {
        if (timeCount > 1f) {
            fps = fpsCount
            fpsCount = 0
            ups = upsCount
            upsCount = 0
            timeCount -= 1f
        }
    }

}