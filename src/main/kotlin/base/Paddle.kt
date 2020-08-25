package base

import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import helper.Vertex

class Paddle {

    companion object{
        val initialDimensions= Vec2(100f,20f)
        val initialVelocity=10f
    }

    var currentPosition= Vec3(Game.resolution.component1()/2,0,1f)
        set(value) {
            field=value
            vertices= Vertex.createVertices(value.component1()-(dimension.component1()/2),0f, dimension.component1(), dimension.component2(),0f,0f,1f)
        }

    val velocity= initialVelocity

    var vertices=Vertex.createVertices(currentPosition.component1()-(initialDimensions.component1()/2),0f, initialDimensions.component1(), initialDimensions.component2(),0f,0f,1f)

    var dimension= initialDimensions
    set(value) {
        field=value
       vertices= Vertex.createVertices(currentPosition.component1()-(value.component1()/2),0f, value.component1(), value.component2(),0f,0f,1f)
    }




}