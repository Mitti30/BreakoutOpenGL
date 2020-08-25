package base.bricks

import base.Brick
import base.BrickCompanion
import glm_.vec2.Vec2
import helper.Vertex

class RedBrick(x: Float, y: Float, width: Float, height: Float,r:Float, g: Float, b: Float) : Brick(x,y,width, height, r,g,b){
    override var remainingHits: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override val companion: BrickCompanion
        get() = TODO("Not yet implemented")
}