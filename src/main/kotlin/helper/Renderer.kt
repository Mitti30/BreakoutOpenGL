package helper

import glm_.vec2.Vec2i
import gln.*
import gln.identifiers.*
import org.lwjgl.opengl.GL11C.*
import org.lwjgl.opengl.GL13C.GL_CLAMP_TO_BORDER
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import java.awt.Color
import java.awt.Font
import java.awt.Font.MONOSPACED
import java.awt.Font.PLAIN
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer


class Renderer {

    //!!! Avoid using nullables on Gl Objects
    private var drawing = false

    private var vao = GlVertexArray(0)
    private var vbo = GlBuffer(0)
    private var program = GlProgram.NULL

    private var fragmentShader = GlShader(0)
    private var vertexShader = GlShader(0)

    private var vertices: FloatBuffer? = null
    private var verticesCount = 0

    private var font = FontHelper(Font(MONOSPACED, PLAIN, 16), true)

    init {
        initShaderProgram()
        loadTexture("./src/main/resources/assets/wall.jpg")

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


    private fun initShaderProgram() {
        vertexShader = compileShader(ShaderType.VERTEX_SHADER, "./src/main/resources/shader/vertex.glsl")
        fragmentShader = compileShader(ShaderType.FRAGMENT_SHADER, "./src/main/resources/shader/fragment.glsl")
        gl.releaseShaderCompiler()
        program = GlProgram.create()

        useShader(fragmentShader, vertexShader)

        vao = gl.genVertexArrays()
        vao.bind()

        vbo = gl.genBuffers()
        vbo.bind(BufferTarget.ARRAY)


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

    private fun initVertexData() {
        TODO("Not yet implemented")
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

        if (verticesCount <= 0) return

        vao = gl.genVertexArrays()
        vao.bind()


        vbo = gl.genBuffers()
        vbo.bind(BufferTarget.ARRAY)

        vbo.data(BufferTarget.ARRAY, vertices!!, Usage.STATIC_DRAW)

        specifyVertexAttributes()
        gl.drawArrays(DrawMode.TRIANGLES, 0, verticesCount)

      //  gl.drawElements(DrawMode.TRIANGLES,verticesCount, IndexType.UNSIGNED_INT,0)
    }

    fun getTextWidth(text: String) = font.getWidth(text)
    fun getTextHeight(text: String) = font.getHeight(text)

    fun drawText(text: String, x: Float, y: Float, color: Color) = font.drawText(this, text, x, y, color)

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
            (Vertex.Type.POSITION.size + Vertex.Type.COLOR.size)*4
        )
    }

    fun drawTextureRegion(
        texture: GlTexture,
        drawX: Float,
        drawY: Float,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        c: Color = Color.WHITE
    ) {

    }

    fun drawBricks(buffer: FloatBuffer) {
        vertices = buffer
        verticesCount = buffer.capacity() / Vertex.size
        flush()
    }

    fun loadTexture(path: String) {


        try {
            val stack = MemoryStack.stackPush()
            /* Prepare image buffers */
            var image: ByteBuffer?
            var width: Int
            var height: Int
            val w: IntBuffer = stack.mallocInt(1)
            val h: IntBuffer = stack.mallocInt(1)
            val comp: IntBuffer = stack.mallocInt(1)

            /* Load image */stbi_set_flip_vertically_on_load(true)
            image = stbi_load(path, w, h, comp, 4)
            if (image == null) {
                throw RuntimeException(
                    "Failed to load a texture file!"
                            + System.lineSeparator() + stbi_failure_reason()
                )
            }

            /* Get width and height of image */
            width = w.get()
            height = h.get()

            createTexture(image,width,height)
        } catch (e: Exception) {
            System.err.println("Failed loading Texture")
        }


    }

    fun createTexture(buffer:ByteBuffer,width: Int,height: Int){
        val texture = gl.genTextures()

        texture.bind(TextureTarget._2D)



        //texture.getImage(0, TextureFormat2.RGB, TextureType2.UNSIGNED_BYTE, image)

        gl.texImage2D(
            Tex2dTarget._2D,
            0,
            InternalFormat.RGBA8,
            Vec2i(width, height),
            gli_.gl.ExternalFormat.RGB,
            gli_.gl.TypeFormat.U8,
            buffer
        )
        gl.generateMipmap(TextureTarget._2D)
        stbi_image_free(buffer)
    }
}