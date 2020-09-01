package base.bricks

import base.Brick
import base.BrickCompanion
import base.ColorMode
import glm_.vec2.Vec2
import glm_.vec3.Vec3ui
import gln.identifiers.GlTexture
import helper.Vertex

class YellowBrick(position: Vec2,dimension: Vec2) : Brick(position, dimension){

    companion object:BrickCompanion{
        override val points=5
        override val texture= GlTexture(0)
        override val color= Vec3ui(255,255,0)
        override val mode= ColorMode.COLOR
        override val initialDimensions=Vec2(50,30)
    }

    override var remainingHits=1

    override val companion= Companion

}