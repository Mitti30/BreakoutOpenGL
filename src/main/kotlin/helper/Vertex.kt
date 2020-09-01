package helper

import base.Game
import glm_.glm
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import java.awt.Color

/**
 * Helper Class for a Vertex
 *
 * @property x Position on x-Axis
 * @property y Position on y-Axis
 * @property r Red color value
 * @property g Green color value
 * @property b Blue color value
 * @property tx Texture position on x-Axis
 * @property ty Texture position on y-Axis
 */
class Vertex(var x: Float, var y: Float, var r: Float, var g: Float, var b: Float, var tx: Float, var ty: Float) {

    companion object{
        val size:Int
            get() = Type.POSITION.size+Type.COLOR.size+Type.TEX_POSITION.size

        /**
         * Creates the 6 Vertices from the Parameters of a Rectangle
         *
         * @param x Center x coordinate
         * @param y Center y coordinate
         * @param width Width of the Rectangle
         * @param height Height of the Rectangle
         * @param r Red color value
         * @param g Green color value
         * @param b Blue color value
         * @param useTexture Should use a texture?
         * @return Array of the 6 calculated Triangle vertices
         */
        fun createVerticesForRectangle(
            x: Float,
            y: Float,
            width: Float,
            height: Float,
            r: Float,
            g: Float,
            b: Float,
            useTexture: Boolean = true
        ): Array<Vertex> {
            return if(useTexture)arrayOf(
                Vertex(x-width/2, y-height/2, r, g, b, 0f, 0f), //bottom / left
                Vertex(x+width/2, y-height/2, r, g, b, 1f, 0f), //bottom / right
                Vertex(x-width/2, y + height/2, r, g, b, 0f, 1f), //top / left

                Vertex(x +width/2, y-height/2, r, g, b, 1f, 0f), // bottom / right
                Vertex(x + width/2, y + height/2, r, g, b, 1f, 1f), // top / right
                Vertex(x-width/2, y + height/2, r, g, b, 0f, 1f) // top / left
            )
            else arrayOf(
                Vertex(x-width/2, y-height/2, r, g, b, 0f, 0f), //bottom / left
                Vertex(x+width/2, y-height/2, r, g, b, 1f, 0f), //bottom / right
                Vertex(x-width/2, y + height/2, r, g, b, 0f, 1f), //top / left

                Vertex(x +width/2, y-height/2, r, g, b, 1f, 0f), // bottom / right
                Vertex(x + width/2, y + height/2, r, g, b, 1f, 1f), // top / right
                Vertex(x-width/2, y + height/2, r, g, b, 0f, 1f) // top / left
            )

        }

        /**
         * Creates the 6 Vertices with Texture Position used for the Fonttexture rendering
         *
         * @param texWidth
         * @param texHeight
         * @param x
         * @param y
         * @param regX
         * @param regY
         * @param regWidth
         * @param regHeight
         * @param c
         * @return Array of the 6 calculated Triangle vertices
         */
        fun createVerticesForFontTexture(
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

    /**
     * Projection Matrix used for converting pixel positions to normalized OpenGl positions
     */
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

    /**
     * Convert Vertex to Normalized Float Array
     *
     * @return Float Array
     */
    fun toFloatArray(): FloatArray {
        val ortho=ortho()
       return floatArrayOf(
           ortho.component1(), ortho.component2(), 1f, r, g, b, tx, ty
       )
    }

    /**
     * Enum with Structure of a Vertex
     *
     * @property size
     */
    enum class Type(val size: Int) {
        POSITION(3),COLOR(3),TEX_POSITION(2)
    }
}
