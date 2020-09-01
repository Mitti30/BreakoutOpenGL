package helper

import gln.TextureTarget
import gln.identifiers.GlTexture
import org.lwjgl.BufferUtils
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage


class FontHelper(private val font: Font, private val antiAlias: Boolean) {

    val glyphs = mutableMapOf<Char, Glyph>()

    var texture = GlTexture(0)

    var texWidth: Float = 0f

    var texHeight = 0f

    private var fontHeight = 0

    init {
        var imageWidth = 0
        var imageHeight = 0

        for (i in 32..255) {
            if (i == 127) {
                continue
            }
            val c = i.toChar()
            val ch = createCharImage(c)
            imageWidth += ch?.width ?: 0
            if (ch != null) {
                imageHeight = imageHeight.coerceAtLeast(ch.height)
            }
        }

        fontHeight = imageHeight

        var image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()

        var x = 0

        for (i in 32..256) {
            if (i == 127) continue

            val c = i.toChar()
            val charImage = createCharImage(c) ?: continue
            
            val charWidth = charImage.width
            val charHeight = charImage.height

            /* Create glyph and draw char on image */
            val ch = Glyph(charWidth, charHeight, x, image.height - charHeight, 0f)
            g.drawImage(charImage, x, 0, null)
            x += ch.width
            glyphs[c] = ch

        }

        /* Flip image Horizontal to get the origin to bottom left */
        val transform = AffineTransform.getScaleInstance(1.0, -1.0)
        transform.translate(0.0, -image.height.toDouble())
        val operation = AffineTransformOp(
            transform,
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
        image = operation.filter(image, null)

        /* Get charWidth and charHeight of image */
        val width = image.width
        val height = image.height

        /* Get pixel data of image */
        val pixels = IntArray(width * height)
        image.getRGB(0, 0, width, height, pixels, 0, width)

        /* Put pixel data into a ByteBuffer */
        val buffer= BufferUtils.createByteBuffer(width * height * 4)
        for (i in 0 until height) {
            for (j in 0 until width) {
                /* Pixel as RGBA: 0xAARRGGBB */
                val pixel = pixels[i * width + j]
                /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */buffer.put(((pixel shr 16) and 0xFF).toByte())
                /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */buffer.put(((pixel shr 8) and 0xFF).toByte())
                /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */buffer.put((pixel and 0xFF).toByte())
                /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */buffer.put(((pixel shr 24) and 0xFF).toByte())
            }
        }
        /* Do not forget to flip the buffer! */
        buffer.flip()

        /* Create texture */
        val fontTexture = GlTexture.createTexture(buffer, width, height)
        texWidth = width.toFloat()
        texHeight = height.toFloat()
        texture = fontTexture

    }

    private fun createCharImage(c: Char): BufferedImage? {

        var image = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        var g = image.createGraphics()
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }
        g.font = font
        val metrics = g.fontMetrics
        g.dispose()

        /* Get char charWidth and charHeight */
        val charWidth = metrics.charWidth(c)
        val charHeight = metrics.height

        /* Check if charWidth is 0 */if (charWidth == 0) {
            return null
        }

        /* Create image for holding the char */
        image = BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB)

        g = image.createGraphics()
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }
        g.font = font
        g.paint = Color.WHITE
        g.drawString(c.toString(), 0, metrics.ascent)
        g.dispose()
        return image

    }

    fun getWidth(text: CharSequence): Int {
        var width = 0
        var lineWidth = 0
        for (element in text) {
            if (element == '\n') {
                /* Line end, set width to maximum from line width and stored
                 * width */
                width = width.coerceAtLeast(lineWidth)
                lineWidth = 0
                continue
            }
            if (element == '\r') {
                /* Carriage return, just skip it */
                continue
            }
            val g: Glyph? = glyphs[element]
            lineWidth += g!!.width
        }
        width = width.coerceAtLeast(lineWidth)
        return width
    }

    fun getHeight(text: CharSequence): Int {
        var height = 0
        var lineHeight = 0
        for (element in text) {
            if (element == '\n') {
                /* Line end, add line height to stored height */
                height += lineHeight
                lineHeight = 0
                continue
            }
            if (element == '\r') {
                /* Carriage return, just skip it */
                continue
            }
            val g: Glyph? = glyphs[element]
            lineHeight = lineHeight.coerceAtLeast(g!!.height)
        }
        height += lineHeight
        return height
    }

    fun drawText(renderer: Renderer, text: CharSequence, x: Float, y: Float, c: Color = Color.WHITE) {
        val textHeight = getHeight(text)
        var drawX = x
        var drawY = y
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight
        }
        texture.bind(TextureTarget._2D)
        renderer.begin()
        for (element in text) {
            val ch = element
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= fontHeight
                drawX = x
                continue
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue
            }
            val g = glyphs[ch]
            g?.let {
                // drawTextureRegion(font.texture, drawX, y, it.x, it.y, it.width, it.height)
                val vert = Vertex.createVerticesForFontTexture(
                    texWidth, texHeight, drawX, y, it.x.toFloat(), it.y.toFloat(),
                    it.width.toFloat(), it.height.toFloat(), c
                )
                vert.forEach { v -> renderer.draw(v.toFloatArray()) }
                drawX += g.width
            }

        }
        renderer.end()
    }
        class Glyph(val width: Int, val height: Int, val x: Int, val y: Int, val advance: Float)



}