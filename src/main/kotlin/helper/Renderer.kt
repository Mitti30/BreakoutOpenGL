package helper

import gln.*
import gln.identifiers.*
import org.lwjgl.BufferUtils
import java.awt.Color
import java.awt.Font
import java.awt.Font.MONOSPACED
import java.awt.Font.PLAIN
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Helper class for rendering Vertices
 *
 * Uses Batch rendering for fewer drawing operations
 */
class Renderer {

    //!!! Avoid using nullables on Gl Objects
    private var drawing = false

    private var vao = GlVertexArray(0)
    private var vbo = GlBuffer(0)
    private var program = GlProgram.NULL

    //Is this enough Memory for us?
    private var vertices=BufferUtils.createFloatBuffer(4096)

    private var verticesCount = 0

    var font = FontHelper(Font(MONOSPACED, PLAIN, 16), true)
    val brickTexture:GlTexture
    val backgroundTexture:GlTexture
    val paddleTexture:GlTexture

    var colorBlending:Boolean=false
        set(value) {
            val b=program.getUniformLocation("shouldBlend")
            if(value)
                program.programUniform(b,1)
            else program.programUniform(b,0)
            field=value
        }

    init {
        initDefaultShaderProgram()
        brickTexture=GlTexture.loadTexture("./src/main/resources/assets/wall.jpg")
        backgroundTexture= GlTexture.loadTexture("./src/main/resources/assets/City2.png")
        paddleTexture=GlTexture.loadTexture("./src/main/resources/assets/paddle.png")

    }

    @Throws(Exception::class)
    private fun readFileAsString(filename: String): String? {
        val source = StringBuilder()
        val `in` = FileInputStream(filename)
        var exception: Exception? = null
        val reader: BufferedReader
        try {
            reader = BufferedReader(InputStreamReader(`in`, "UTF-8"))
            var innerExc: Exception? = null
            try {
                var line: String?
                while (reader.readLine().also { line = it } != null) source.append(line).append('\n')
            } catch (exc: Exception) {
                exception = exc
            } finally {
                try {
                    reader.close()
                } catch (exc: Exception) {
                    if (innerExc == null) innerExc = exc else exc.printStackTrace()
                }
            }
            if (innerExc != null) throw innerExc
        } catch (exc: Exception) {
            exception = exc
        } finally {
            try {
                `in`.close()
            } catch (exc: Exception) {
                if (exception == null) exception = exc else exc.printStackTrace()
            }
            if (exception != null) throw exception
        }
        return source.toString()
    }

    private fun compileShader(type: ShaderType, path: String): GlShader {

        val shader = gl.createShader(type)
        shader.source(readFileAsString(path)!!)

        shader.compile()

        if (!shader.compileStatus)
            throw RuntimeException("Shader Compile Error ${shader.infoLog}")

        return shader

    }


    private fun initDefaultShaderProgram() {
       val vertexShader = compileShader(ShaderType.VERTEX_SHADER, "./src/main/resources/shader/vertex.glsl")
       val fragmentShader = compileShader(ShaderType.FRAGMENT_SHADER, "./src/main/resources/shader/fragment.glsl")
        gl.releaseShaderCompiler()
        program = GlProgram.create()

        useShader(fragmentShader, vertexShader)

        vao = gl.genVertexArrays()
        vao.bind()

        vbo = gl.genBuffers()
        vbo.bind(BufferTarget.ARRAY)

        //OpenGL allocates our memory because we want to fill it later
        vbo.data(BufferTarget.ARRAY, vertices.capacity() * 4, Usage.DYNAMIC_DRAW)

        specifyVertexAttributes()
    }

    fun useShader(fragmentShader: GlShader, vertexShader: GlShader) {

        with(program) {
            attach(fragmentShader)
            attach(vertexShader)
            link()
            detach(fragmentShader)
            detach(vertexShader)
        }
        fragmentShader.delete()
        vertexShader.delete()

        program.use()

    }

    fun begin() {
        check(!drawing) { "Renderer is already drawing!" }
        drawing = true
        verticesCount = 0
    }

    /**
     * End rendering.
     */
    fun end() {
        check(drawing) { "Renderer isn't drawing!" }
        drawing = false
        flush()
    }

    fun flush() {

        if (verticesCount <= 0) return else vertices.flip()



        vbo.subData(BufferTarget.ARRAY, vertices)
        //gl.bufferSubData(vbo,0,vertices)
        //vbo.data(BufferTarget.ARRAY,vertices, Usage.DYNAMIC_DRAW)

        gl.drawArrays(DrawMode.TRIANGLES, 0, verticesCount)
         //gl.drawElements(DrawMode.TRIANGLES,verticesCount, IndexType.UNSIGNED_INT,0)
        vertices.clear()
        verticesCount=0


    }

    fun getTextWidth(text: String) = font.getWidth(text)
    fun getTextHeight(text: String) = font.getHeight(text)

    fun clear() {
        gl.clear(ClearBufferMask.COLOR_BUFFER_BIT)
        gl.clear(ClearBufferMask.DEPTH_BUFFER_BIT)
    }

    private fun specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        gl.enableVertexAttribArray(Vertex.Type.POSITION.ordinal)
        gl.vertexAttribPointer(0, Vertex.Type.POSITION.size, VertexAttrType.FLOAT, false, Vertex.size * 4, 0)

        /* Specify Color Pointer */
        gl.enableVertexAttribArray(Vertex.Type.COLOR.ordinal)
        gl.vertexAttribPointer(
            1,
            Vertex.Type.COLOR.size,
            VertexAttrType.FLOAT,
            true,
            Vertex.size * 4,
            Vertex.Type.POSITION.size * 4
        )

        /* Specify Texture Pointer */
        gl.enableVertexAttribArray(Vertex.Type.TEX_POSITION.ordinal)
        gl.vertexAttribPointer(
            2,
            Vertex.Type.TEX_POSITION.size,
            VertexAttrType.FLOAT,
            false,
            Vertex.size * 4,
            (Vertex.Type.POSITION.size + Vertex.Type.COLOR.size) * 4
        )
    }


    fun draw(array: FloatArray) {
        if(vertices.remaining()<Vertex.size*6)
            flush()

        vertices.put(array)
        verticesCount+=array.size/Vertex.size
    }

    fun drawText(text: String, x: Float, y: Float)=font.drawText(this,text, x, y)
}