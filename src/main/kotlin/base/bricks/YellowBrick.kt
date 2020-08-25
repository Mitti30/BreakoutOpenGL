package base.bricks

import base.Brick
import base.BrickCompanion
import glm_.vec2.Vec2
import helper.Vertex

class YellowBrick(x: Float, y: Float, width: Float, height: Float,r:Float, g: Float, b: Float) : Brick(x,y,width, height, r,g,b){

    companion object: BrickCompanion {
        override val points=3
    }

    override var remainingHits: Int=3

    override val companion= Companion

}