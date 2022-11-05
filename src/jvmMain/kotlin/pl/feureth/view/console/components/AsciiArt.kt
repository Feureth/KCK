package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import pl.feureth.view.console.common.Drawer
import java.io.File

class AsciiArt(
    drawer: Drawer,
    textFilePath: String,
    private val colorStyle: Drawer.ColorStyle = Drawer.ColorStyle.default
) : ConsoleComponent(drawer) {

    private val frame: AsciiFrame

    init {
        val file = File(this::class.java.classLoader.getResource(textFilePath)?.file ?: "")
        frame = AsciiFrame(lines = file.readLines())
    }

    override fun draw() {
        drawer.setColorStyle(colorStyle)
        frame.draw(drawer)
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return false
    }

    private data class AsciiFrame(
        val lines: List<String>
    ) {
        fun draw(drawer: Drawer) {
            lines.forEachIndexed { index, text ->
                drawer.putString(0, index, text)
            }
        }
    }
}