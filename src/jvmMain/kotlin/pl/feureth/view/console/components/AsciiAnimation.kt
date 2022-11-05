package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import kotlinx.coroutines.*
import pl.feureth.view.console.ConsoleView
import pl.feureth.view.console.common.Drawer
import java.io.File

class AsciiAnimation(
    drawer: Drawer,
    textFilesPath: String,
    private val colorStyle: Drawer.ColorStyle = Drawer.ColorStyle.default
) : ConsoleComponent(drawer) {

    private val frames = mutableListOf<AsciiFrame>()
    private var currentFrame: Int = 0

    private val scope = MainScope()
    private var job: Job? = null

    init {
        val directory = File(this::class.java.classLoader.getResource(textFilesPath)?.file ?: "")
        if (directory.isDirectory.not()) throw IllegalArgumentException("Path $textFilesPath is not directory")
        directory.walk().sortedBy { it.name }.forEach { file ->
            if (file.name.endsWith(".txt"))
                frames.add(AsciiFrame(lines = file.readLines()))
        }
    }

    override fun draw() {
        drawer.setColorStyle(colorStyle)
        frames[currentFrame].draw(drawer)
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return false
    }

    fun start() {
        stop()
        job = scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(1000L / FPS)
                ensureActive()
                currentFrame++
                if (currentFrame >= frames.size) currentFrame = 0
                drawer.clearContent()
                draw()
                ensureActive()
                ConsoleView.screen.refresh()
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
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

    companion object {
        private const val FPS = 10
    }
}