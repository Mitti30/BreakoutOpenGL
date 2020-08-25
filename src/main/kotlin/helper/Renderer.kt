package helper

import glm_.BYTES
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
import java.nio.FloatBuffer

class Renderer() {

    //!!! Avoid using nullables on Gl Objects
    private var drawing=false

    private var vao=GlVertexArray(0)
    private var vbo=GlBuffer(0)
    private var program=GlProgram.NULL

    private var fragmentShader=GlShader(0)
    private var vertexShader=GlShader(0)

    private var vertices:FloatBuffer?=null
    private var verticesCount=0

    private var font=FontHelper(Font(MONOSPACED, PLAIN, 16), true)

    init {
        initShaderProgram()

        gl.enable(State.BLEND)

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

    private fun compileShader(type: ShaderType, path: String):GlShader {

        val shader = gl.createShader(type)
        shader.source(readFileAsString(path)!!)

        shader.compile()

        if(!shader.compileStatus)
            throw RuntimeException("Shader Compile Error ${shader.infoLog}")

        return shader

    }


    private fun initShaderProgram() {
        vertexShader=compileShader(ShaderType.VERTEX_SHADER,"./src/main/resources/shader/vertex.glsl")
        fragmentShader=compileShader(ShaderType.FRAGMENT_SHADER,"./src/main/resources/shader/fragment.glsl")
        gl.releaseShaderCompiler()
        program=GlProgram.create()

        useShader(fragmentShader,vertexShader)

        vao= gl.genVertexArrays()
        vao.bind()

        vbo=gl.genBuffers()
        vbo.bind(BufferTarget.ARRAY)




    }

    fun useShader(fragmentShader: GlShader, vertexShader: GlShader){

        with(program){
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

    fun flush(){

        if(verticesCount<=0) return

        vao=gl.genVertexArrays()
        vao.bind()


        vbo=gl.genBuffers()
        vbo.bind(BufferTarget.ARRAY)

        vbo.data(BufferTarget.ARRAY,vertices!!, Usage.STATIC_DRAW)

        specifyVertexAttributes()

        gl.drawArrays(DrawMode.TRIANGLES,0,verticesCount)




    }

    fun getTextWidth(text: String)=font.getWidth(text)
    fun getTextHeight(text: String)=font.getHeight(text)

    fun drawText(text:String,x:Float,y:Float,color:Color)=font.drawText(this,text,x,y,color)

    fun clear(){
        gl.clear(ClearBufferMask.COLOR_BUFFER_BIT)
        gl.clear(ClearBufferMask.DEPTH_BUFFER_BIT)
    }

    private fun specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        gl.enableVertexAttribArray(Vertex.Type.POSITION.ordinal)
        gl.vertexAttribPointer(0,3, VertexAttrType.FLOAT,false,Vertex.size*4,0)

        /* Specify Color Pointer */
        gl.enableVertexAttribArray(Vertex.Type.COLOR.ordinal)
        gl.vertexAttribPointer(1,3, VertexAttrType.FLOAT,true,Vertex.size*4,Vertex.Type.POSITION.size*4)

        /* Specify Texture Pointer */
      //  gl.enableVertexAttribArray(Vertex.Type.TEX_POSITION.ordinal)
      //  gl.vertexAttribPointer(2,2, VertexAttrType.FLOAT,false,Vertex.size*4,Vertex.Type.POSITION.size+Vertex.Type.COLOR.size)
    }

    fun drawTextureRegion(texture: GlTexture, drawX: Float, drawY: Float, x: Int, y: Int, width: Int, height: Int, c: Color=Color.WHITE) {

    }

    fun drawBricks(buffer:FloatBuffer){
    vertices=buffer
        verticesCount=12
        flush()
    }

}