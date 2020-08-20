import base.Brick
import base.Game
import base.YellowBrick
import gln.gl
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.system.jawt.JAWT
import uno.awt.LwjglCanvas
import uno.glfw.GlfwWindow
import uno.glfw.glfw

fun main() {

    glfw.init()

    val game = Game
    val window = GlfwWindow(game.resolution, game.title).apply {
        init()
    resizable=false}
    gl.viewport(game.resolution)
    GL.createCapabilities()

    glEnable(GL_TEXTURE_2D)

        game.createAndStartNewLevel(window)
    window.loop {

    }

}