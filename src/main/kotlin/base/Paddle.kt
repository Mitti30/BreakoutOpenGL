package base

import glm_.vec2.Vec2
import glm_.vec2.Vec2i

class Paddle {

    companion object{
        val initialDimensions= Vec2(100f,20f)
        val initialVelocity=100f
    }

    val dimension= initialDimensions
    val velocity= initialVelocity


}