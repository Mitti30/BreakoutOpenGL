package base

import glm_.vec2.Vec2
import glm_.vec3.Vec3ui
import gln.identifiers.GlTexture
import helper.Vertex
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.random.nextInt

class Ball(private val paddle:Paddle,position: Vec2=Vec2(paddle.currentPosition.component1(),paddle.currentPosition.component2()+paddle.dimension.component2()+ initialRadius), dimension: Vec2= initialDimensions):GameObject(position, dimension) {

    companion object:GameObjectCompanion{
        const val initialRadius=15f
        const val velocity=200f
        override val texture= GlTexture(0)
        override val color= Vec3ui(0,0,255)
        override val mode=ColorMode.COLOR
        override val initialDimensions= Vec2(15,15)
    }

    override val companion=Companion

    private var sticky=true

    private var direction:Vec2

    override lateinit var vertices:Array<Vertex>

    init {
        val rand = Random.nextInt(0..100)

       direction= when(rand){
            in 0 until 25-> Vec2((-cos(Math.toRadians(45.0))).toFloat(),(-sin(Math.toRadians(45.0))).toFloat())
            in 25 until 50-> Vec2((-cos(Math.toRadians(45.0))).toFloat(),sin(Math.toRadians(45.0)).toFloat())
           in 50 until 75 -> Vec2(cos(Math.toRadians(45.0)).toFloat(),(-sin(Math.toRadians(45.0))).toFloat())
           else-> Vec2(cos(Math.toRadians(45.0)).toFloat(),sin(Math.toRadians(45.0)).toFloat())
        }

    }

    fun update(delta: Double)
    {
        if(sticky){
            vertices=Vertex.createVerticesForRectangle(
                paddle.currentPosition.component1(), paddle.currentPosition.component2()+paddle.dimension.component2() + initialRadius, initialRadius*2,
                initialRadius*2, 0f, 0f, 1f, true
            )
            return
        }


            val current= Vec2(vertices.first().x,vertices.first().y)
            var h=direction* velocity

            var new=current +( h*delta)

        vertices=Vertex.createVerticesForRectangle(
            new.component1(), new.component2(), initialRadius*2,
            initialRadius*2, 0f, 0f, 1f, true
        )

    }

    fun throwBall(){
        sticky=false
    }

    fun checkCollision(list: ArrayList<GameObject>) {

    }

}