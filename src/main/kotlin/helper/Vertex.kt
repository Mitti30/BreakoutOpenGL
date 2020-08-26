package helper

import base.Game
import glm_.glm
import glm_.vec3.Vec3
import glm_.vec4.Vec4

class Vertex(var x:Float,var y:Float,var r:Float,var g:Float, var b:Float,var tx:Float,var ty:Float) {

    companion object{
        val size:Int
            get() = Type.POSITION.size+Type.COLOR.size+Type.TEX_POSITION.size


        fun createVertices(x:Float,y:Float,width:Float,height:Float,r:Float,g:Float,b:Float,texture:Boolean=true): Array<Vertex> {
            return if(texture)arrayOf(
                Vertex(x, y, r, g, b, 0f, 0f), //bottom / left
                Vertex(x + width, y, r, g, b, 1f, 0f), //bottom / right
                Vertex(x, y + height, r, g, b, 0f, 1f), //top / left

                Vertex(x + width, y, r, g, b, 1f, 0f), // bottom / right
                Vertex(x + width, y + height, r, g, b, 1f, 1f), // top / right
                Vertex(x, y + height, r, g, b, 0f, 1f) // top / left
            )
            else arrayOf(
                Vertex(x, y, r, g, b, 0f, 0f), //bottom / left
                Vertex(x + width, y, r, g, b, 0f, 0f), //bottom / right
                Vertex(x, y + height, r, g, b, 0f, 0f), //top / left

                Vertex(x + width, y, r, g, b, 0f, 0f), // bottom / right
                Vertex(x + width, y + height, r, g, b, 0f, 0f), // top / right
                Vertex(x, y + height, r, g, b, 0f, 0f) // top / left
            )

        }
    }

    private val matrix= glm.ortho(0f, Game.resolution.component1().toFloat(),0f, Game.resolution.component2().toFloat(),0.1f,100f)

    fun ortho():Vec3{
        val vec =matrix * Vec4(x,y,1f,1f)
        return Vec3(vec.component1(),vec.component2(),vec.component3())
    }

    fun toFloatArray(): FloatArray {
        val ortho=ortho()
       return floatArrayOf(
            ortho.component1(),ortho.component2(),1f,r,g,b,tx,ty
        )
    }

    enum class Type(val size:Int) {
        POSITION(3),COLOR(3),TEX_POSITION(2)
    }
}
