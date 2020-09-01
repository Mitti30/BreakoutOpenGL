import base.Game
import gln.gl
import helper.LevelCreator
import helper.Renderer
import helper.Vertex
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.system.Configuration
import uno.glfw.GlfwWindow
import uno.glfw.glfw

fun main() {
  //Configuration.DEBUG_LOADER.set(true)
    //Configuration.DEBUG_MEMORY_ALLOCATOR.set(true)
    val game=Game
  game.start()

    game.end()



}