package helper

import glm_.vec2.Vec2i
import gln.InternalFormat
import gln.Tex2dTarget
import gln.TextureTarget
import gln.gl
import gln.identifiers.GlTexture
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import java.nio.IntBuffer

fun GlTexture.Companion.loadTexture(path: String): GlTexture {

    val stack = MemoryStack.stackPush()
    try {

        /* Prepare image buffers */
        var image: ByteBuffer?
        var width: Int
        var height: Int
        val w: IntBuffer = stack.mallocInt(1)
        val h: IntBuffer = stack.mallocInt(1)
        val comp: IntBuffer = stack.mallocInt(1)

        /* Load image */STBImage.stbi_set_flip_vertically_on_load(true)
        image = STBImage.stbi_load(path, w, h, comp, 4)
        if (image == null) {
            throw RuntimeException(
                "Failed to load a texture file!"
                        + System.lineSeparator() + STBImage.stbi_failure_reason()
            )
        }

        /* Get width and height of image */
        width = w.get()
        height = h.get()

        return createTexture(image, width, height)
    } catch (e: Exception) {
        System.err.println("Failed loading Texture")
    }finally {
        stack.close()
    }

    return GlTexture(0)
}

fun GlTexture.Companion.createTexture(buffer: ByteBuffer, width: Int, height: Int): GlTexture {
    val texture = gl.genTextures()

    texture.bind(TextureTarget._2D)

    gl.texImage2D(
        Tex2dTarget._2D,
        0,
        InternalFormat.RGBA8,
        Vec2i(width, height),
        gli_.gl.ExternalFormat.RGBA,
        gli_.gl.TypeFormat.U8,
        buffer
    )
    gl.generateMipmap(TextureTarget._2D)
    return texture
}