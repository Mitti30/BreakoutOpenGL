package base.gamestates

import base.Brick
import base.Game
import base.GameStateInterface
import base.Paddle
import base.bricks.YellowBrick
import glm_.vec2.Vec2
import gln.glf.Vertex
import helper.Renderer
import org.lwjgl.BufferUtils
import java.awt.Color
import java.nio.FloatBuffer
import kotlin.properties.Delegates


//no level without a game
class Level : GameStateInterface {

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
    }


    fun calculateVerticesFromList() {
        //Two Triangles(3) per Brick * 6 (X, Y, Z, R, G, B)
        brickVertices = BufferUtils.createFloatBuffer(brickList.size * 6 *helper.Vertex.size)
        brickList.forEach {brick->
            brick.vertexes.forEach {vertex->
                brickVertices.put(vertex.toFloatArray())
            } }
        brickVertices.flip()
        val arr=ArrayList<FloatArray>()
        brickList.forEach {brick->
            brick.vertexes.forEach {vertex->
                arr.add(vertex.toFloatArray())
            }
        }
        println(arr.size)

    }

    override fun render() {
        renderer.clear()

        calculateVerticesFromList()

        renderer.begin()
        renderer.drawBricks(brickVertices)
        renderer.end()

        // Draw Level Name
        /*val scoreText = "Score"
        val scoreTextWidth: Int = renderer.getTextWidth(scoreText)
        val scoreTextHeight: Int = renderer.getTextHeight(scoreText)
        val scoreTextX: Float = (Game.resolution.component1() - scoreTextWidth) / 2f
        val scoreTextY: Float = Game.resolution.component2() - scoreTextHeight.toFloat() - 5
        renderer.drawText(scoreText, scoreTextX, scoreTextY, Color.BLACK) */



    }

}