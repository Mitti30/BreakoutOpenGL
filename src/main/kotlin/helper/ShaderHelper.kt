package helper

import gln.ShaderType
import gln.gl
import gln.identifiers.GlShader
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

object ShaderHelper{


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

    fun compileShader(type: ShaderType, path: String): GlShader {

        val shader = gl.createShader(type)
        shader.source(readFileAsString(path)!!)

        shader.compile()

        if(!shader.compileStatus)
            throw RuntimeException("ShaderHelper Compile Error ${shader.infoLog}")

        return shader

    }


}