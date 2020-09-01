package base

import base.bricks.GreenBrick
import base.bricks.RedBrick
import base.bricks.YellowBrick
import glm_.vec2.Vec2
import helper.Vertex
import kotlin.reflect.KClass

abstract class Brick(position: Vec2, dimension: Vec2):GameObject(position, dimension) {

    override var vertices:Array<Vertex> = Vertex.createVerticesForRectangle(position.x,position.y,dimension.x,dimension.y,companion.color.x.toFloat(),companion.color.y.toFloat(),companion.color.z.toFloat())

    abstract var remainingHits: Int
        protected set

    var destroyed=false
    private set


    fun hit() {
        remainingHits--
        if(remainingHits<=0) destroyed=true
        //TODO: Change Appearance and play sound
    }

}




interface BrickCompanion:GameObjectCompanion {
    val points: Int
}