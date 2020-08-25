package base.bricks

import base.Brick
import base.BrickCompanion
import glm_.vec2.Vec2
import helper.Vertex

class GreenBrick(x: Float, y: Float, width: Float, height: Float,r:Float, g: Float, b: Float) : Brick(x,y,width, height, r,g,b) {

    companion object: BrickCompanion {
        override val points: Int=5
    }

    override var remainingHits: Int=2
    override val companion: BrickCompanion = Companion

}