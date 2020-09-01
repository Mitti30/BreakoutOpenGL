package base.gamestates

import base.*
import base.bricks.YellowBrick
import glm_.vec2.Vec2
import helper.Renderer
import org.lwjgl.glfw.GLFW
import uno.glfw.GlfwWindow
import uno.glfw.KeyCB
import java.nio.FloatBuffer
import kotlin.properties.Delegates
import glm_.vec3.Vec3
import gln.TextureTarget
import helper.LevelCreator
import helper.Vertex
import uno.glfw.Key
import java.awt.Color


//no level without a game
class Level(private val window: GlfwWindow) : GameStateBase(window) {

    var finished = false
        private set

    var maximumPoints by Delegates.notNull<Int>()
        private set

    var currentPoints: Int = 0
        private set

    val currentBalls = 3

    private val creator=LevelCreator(Game.resolution[0].toFloat(), Game.resolution[1].toFloat(),14,8,0f,0f)

    private val brickList = ArrayList<Brick>()

    val paddle = Paddle()

    val ball=Ball(paddle)

    override val renderer = Renderer()

    private lateinit var brickVertices: FloatBuffer

    var currentKeyPressed:Key=Key.of(320)

    init {


        window.keyCB=object: KeyCB{
            override fun invoke(key: Int, scanCode: Int, action: Int, mods: Int) {
                /*when(key){
                    GLFW.GLFW_KEY_LEFT->
                        if(paddle.currentPosition.component1()-paddle.dimension.component1()/2>0)
                        paddle.currentPosition=Vec3(paddle.currentPosition.component1()-paddle.velocity,paddle.currentPosition.component2(),paddle.currentPosition.component3())
                    GLFW.GLFW_KEY_RIGHT->
                        if(paddle.currentPosition.component1()+paddle.dimension.component1()/2< Game.resolution.component1())
                        paddle.currentPosition=Vec3(paddle.currentPosition.component1()+paddle.velocity,paddle.currentPosition.component2(),paddle.currentPosition.component3())
                } */
                if (action==GLFW.GLFW_PRESS)
                currentKeyPressed=Key.of(key)

                if(action==GLFW.GLFW_RELEASE && Key.of(key)==currentKeyPressed)
                    currentKeyPressed=Key.of(320)
            }

        }
    }

    override fun update(delta: Double) {
        val list=ArrayList<GameObject>()
        ball.update(delta)
        ball.checkCollision(list)

    }

    override fun handleInput() {
        when(currentKeyPressed){
            Key.LEFT-> {
                val new= Vec3(
                    paddle.currentPosition.component1() - paddle.velocity,
                    paddle.currentPosition.component2(),
                    paddle.currentPosition.component3()
                )
                if(new.component1()-paddle.dimension.component1()/2<0)
                    new.r=paddle.dimension.component1()/2
                paddle.currentPosition=new
            }
            Key.RIGHT->{
                val new= Vec3(
                    paddle.currentPosition.component1() + paddle.velocity,
                    paddle.currentPosition.component2(),
                    paddle.currentPosition.component3()
                )
                if(new.component1()+paddle.dimension.component1()/2>Game.resolution.component1())
                    new.r=Game.resolution.component1()-paddle.dimension.component1()/2
                paddle.currentPosition=new
            }
            Key.SPACE->ball.throwBall()
            else -> {/*Nothing to do here*/}
        }
    }

    override fun render() {
        renderer.clear()
        renderer.colorBlending=false
        renderer.backgroundTexture.bind(TextureTarget._2D)
        renderer.begin()

            Vertex.createVerticesForRectangle(Game.resolution.component1()/2f,Game.resolution.component2()/2f,Game.resolution.component1().toFloat(),Game.resolution.component2().toFloat(),1f,1f,0f).forEach {
                renderer.draw(it.toFloatArray())
            }
        renderer.flush()
        renderer.paddleTexture.bind(TextureTarget._2D)
        paddle.vertices.forEach {
            val v=Vec2(it.x,it.y)

            renderer.draw(it.toFloatArray()) }
        ball.vertices.forEach { renderer.draw(it.toFloatArray()) }
        renderer.end()

        renderer.brickTexture.bind(TextureTarget._2D)
        renderer.colorBlending=true

        renderer.begin()
        brickList.forEach {brick->
            brick.vertices.forEach { vertex->
              renderer.draw(vertex.toFloatArray())
            } }

        renderer.end()

        // Draw Level Name
        val scoreText = "Score"
        val scoreTextWidth: Int = renderer.getTextWidth(scoreText)
        val scoreTextHeight: Int = renderer.getTextHeight(scoreText)
        renderer.drawText("Level: ${Game.currentLevelPosition+1}",20f,Game.resolution.component2()-20f)
        renderer.drawText("FPS: " + Game.timer.fps + " | UPS: " + Game.timer.ups, 100f, 100f)
    }

}