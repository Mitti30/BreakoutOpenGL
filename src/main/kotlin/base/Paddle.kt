package base

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.Vec3ui
import gln.identifiers.GlTexture
import helper.Vertex
import helper.loadTexture

class Paddle(dimension:Vec2= Vec2(100f,50f)):GameObject(Vec2(0,dimension.component2()/2), dimension){

    companion object:GameObjectCompanion{
        const val initialVelocity=20f
        override val texture= GlTexture.loadTexture("./src/main/resources/assets/paddle.png")
        override val color= Vec3ui(0,0,255)
        override val mode=ColorMode.Texture
        override val initialDimensions=Vec2(100f,30f)
    }

    override val companion=Companion

    var currentPosition= Vec3(Game.resolution.component1()/2,0,1f)
        set(value) {
            field=value
            vertices= Vertex.createVerticesForRectangle(value.component1(),dimension.component2()/2, dimension.component1(), dimension.component2(),0f,0f,1f,true)
        }

    val velocity= initialVelocity

    override var vertices:Array<Vertex> = Vertex.createVerticesForRectangle(currentPosition.component1() ,
        dimension.component2()/2, initialDimensions.component1(), initialDimensions.component2(),0f,0f,1f,true)
        get() {

        return Vertex.createVerticesForRectangle(currentPosition.component1() ,
            dimension.component2()/2, initialDimensions.component1(), initialDimensions.component2(),0f,0f,1f,true)

    }






}