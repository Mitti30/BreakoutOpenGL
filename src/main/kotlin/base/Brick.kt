package base

import glm_.vec2.Vec2
import helper.Vertex

abstract class Brick(x:Float,y:Float,width:Float,height:Float,r:Float,g:Float,b:Float) {

    val vertices:Array<Vertex> = Vertex.createVertices(x,y,width,height, r, g, b)

    abstract var remainingHits: Int
        protected set

    var destroyed=false
    private set

    //Important for the Inheritance so we don't need to look up what type of brick it is
    abstract val companion: BrickCompanion


    fun hit() {
        remainingHits--
        if(remainingHits<=0) destroyed=true
        //TODO: Change Appearance and play sound
    }

}




interface BrickCompanion {
    val points: Int
}