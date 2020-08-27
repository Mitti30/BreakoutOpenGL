package helper

import base.Game
import glm_.glm
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import java.awt.Color

class Vertex(var x: Float, var y: Float, var r: Float, var g: Float, var b: Float, var tx: Float, var ty: Float) {

    companion object{
        val size:Int
            get() = Type.POSITION.size+Type.COLOR.size+Type.TEX_POSITION.size


        fun createVertices(
            x: Float,
            y: Float,
            width: Float,
            height: Float,
            r: Float,
            g: Float,
            b: Float,
            texture: Boolean = true
        ): Array<Vertex> {
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

        fun createVertices(
            texWidth: Float,
            texHeight: Float,
            x: Float,
            y: Float,
            regX: Float,
            regY: Float,
            regWidth: Float,
            regHeight: Float,
            c: Color
        ):Array<Vertex>{

            /* Vertex positions */
            val x2 = x + regWidth
            val y2 = y + regHeight

            val r = c.red.toFloat()
            val g = c.green.toFloat()
            val b = c.blue.toFloat()

            /* Texture coordinates */
            val s1: Float = regX / texWidth
            val t1: Float = regY / texHeight
            val s2: Float = (regX + regWidth) / texWidth
            val t2: Float = (regY + regHeight) / texHeight

            return arrayOf(
                Vertex(x, y, r, g, b, s1, t1), //bottom / left
                Vertex(x, y2, r, g, b, s1, t2), //bottom / right
                Vertex(x2, y2, r, g, b, s2, t2), //top / left

                Vertex(x, y, r, g, b, s1, t1), // bottom / right
                Vertex(x2, y2, r, g, b, s2, t2), // top / right
                Vertex(x2, y, r, g, b, s2, t1) // top / left
            )
        }

    }

    private val matrix= glm.ortho(
        0f,
        Game.resolution.component1().toFloat(),
        0f,
        Game.resolution.component2().toFloat(),
        0.1f,
        100f
    )

    fun ortho():Vec3{
        val vec =matrix * Vec4(x, y, 1f, 1f)
        return Vec3(vec.component1(), vec.component2(), vec.component3())
    }

    fun toFloatArray(): FloatArray {
        val ortho=ortho()
       return floatArrayOf(
           ortho.component1(), ortho.component2(), 1f, r, g, b, tx, ty
       )
    }

    enum class Type(val size: Int) {
        POSITION(3),COLOR(3),TEX_POSITION(2)
    }
}
