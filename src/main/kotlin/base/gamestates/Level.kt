package base.gamestates

import base.Brick
import base.Game
import base.GameStateBase
import base.Paddle
import base.bricks.YellowBrick
import helper.Renderer
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import uno.glfw.GlfwWindow
import uno.glfw.KeyCB
import java.nio.FloatBuffer
import kotlin.properties.Delegates
import glm_.vec3.Vec3
import gln.TextureTarget
import helper.FontHelper
import java.awt.Color
import java.awt.Font


//no level without a game
class Level(window: GlfwWindow) : GameStateBase(window) {

    var finished = false
        private set

    var maximumPoints by Delegates.notNull<Int>()
        private set

    var currentPoints: Int = 0
        private set

    val currentBalls = 3

    private val brickList = ArrayList<Brick>()

    val paddle = Paddle()

    override val renderer = Renderer()

    private lateinit var brickVertices: FloatBuffer

    init {
        brickList.add(YellowBrick(10f,10f,150f,100f,1f,1f,0f))
        brickList.add(YellowBrick(500f,500f,150f,100f,1f,1f,0f))

        window.keyCB=object: KeyCB{
            override fun invoke(key: Int, scanCode: Int, action: Int, mods: Int) {
                when(key){
                    GLFW.GLFW_KEY_LEFT->
                        if(paddle.currentPosition.component1()-paddle.dimension.component1()/2>0)
                        paddle.currentPosition=Vec3(paddle.currentPosition.component1()-paddle.velocity,paddle.currentPosition.component2(),paddle.currentPosition.component3())
                    GLFW.GLFW_KEY_RIGHT->
                        if(paddle.currentPosition.component1()+paddle.dimension.component1()/2< Game.resolution.component1())
                        paddle.currentPosition=Vec3(paddle.currentPosition.component1()+paddle.velocity,paddle.currentPosition.component2(),paddle.currentPosition.component3())
                }
            }

        }
    }


    fun calculateVerticesFromList() {
        //Two Triangles(3) per Brick * 6 (X, Y, Z, R, G, B)
        brickVertices = BufferUtils.createFloatBuffer(brickList.size * 6 *helper.Vertex.size + 6*helper.Vertex.size)
        brickList.forEach {brick->
            brick.vertices.forEach { vertex->
                brickVertices.put(vertex.toFloatArray())
            } }
        paddle.vertices.forEach {
            brickVertices.put(it.toFloatArray())
        }
        brickVertices.flip()
    }

    override fun render() {
        renderer.clear()
        renderer.brick.bind(TextureTarget._2D)
        renderer.begin()
        brickList.forEach {brick->
            brick.vertices.forEach { vertex->
              renderer.draw(vertex.toFloatArray())
            } }
        paddle.vertices.forEach { renderer.draw(it.toFloatArray()) }
        renderer.end()

        // Draw Level Name
        val scoreText = "Score"
        val scoreTextWidth: Int = renderer.getTextWidth(scoreText)
        val scoreTextHeight: Int = renderer.getTextHeight(scoreText)
        val scoreTextX: Float = (640 - scoreTextWidth) / 2f
        val scoreTextY: Float = 480 - scoreTextHeight.toFloat() - 5
        FontHelper(Font(Font.MONOSPACED, Font.PLAIN, 24),true).drawText(renderer,scoreText, 50f, 50f, Color.BLACK)



    }

}