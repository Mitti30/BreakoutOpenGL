package helper

import gln.TextureTarget
import gln.identifiers.GlTexture
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage


class FontHelper(private val font: Font, private val antiAlias: Boolean) {

    private val glyphs= mapOf<Char, Glyph>()

    private var texture=GlTexture(0)

    private val fontHeight = 0

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

        val fontHeight = imageHeight

        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()
    }

    private fun createCharImage(c: Char):BufferedImage?{

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

    fun drawText(renderer: Renderer, text: CharSequence, x: Float, y: Float, c: Color=Color.WHITE) {
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
            renderer.drawTextureRegion(texture, drawX, drawY, g!!.x, g.y, g.width, g.height, c)
            drawX += g.width.toFloat()
        }
        renderer.end()
    }

    class Glyph(val width: Int, val height: Int, val x: Int, val y: Int)


}