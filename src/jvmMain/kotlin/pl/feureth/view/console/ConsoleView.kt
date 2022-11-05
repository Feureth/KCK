package pl.feureth.view.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import pl.feureth.view.AppView
import pl.feureth.view.console.layout.LayoutNavigator
import pl.feureth.view.console.layout.impl.MainMenuScreen
import java.io.IOException
import javax.swing.JFrame

class ConsoleView : AppView {

    override fun start() {
        initTerminal()
        startMainLoop()
    }

    @Throws(IOException::class)
    private fun initTerminal() {
        terminal = DefaultTerminalFactory()
            .setInitialTerminalSize(TerminalSize(WINDOW_WIDTH, WINDOW_HEIGHT))
            .setTerminalEmulatorTitle("Biblioteka")
            .createTerminal()
        (terminal as? SwingTerminalFrame)?.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        screen = TerminalScreen(terminal)
        graphics = screen.newTextGraphics()
        screen.startScreen()
    }

    private fun closeTerminal() {
        screen.stopScreen()
    }

    private fun startMainLoop() {

        val layout = MainMenuScreen()
        LayoutNavigator.goTo(layout)

        var isExit = false
        while (isExit.not()) {
            val keyStroke = screen.readInput()
            when (keyStroke?.keyType) {
                KeyType.Escape -> {
                    if (LayoutNavigator.back().not()) {
                        isExit = true
                    }
                }
                else -> {
                    if (LayoutNavigator.current?.performInput(keyStroke) == true) {
                        screen.refresh()
                    }
                }
            }
        }
        closeTerminal()
    }

    companion object {
        const val WINDOW_WIDTH = 120
        const val WINDOW_HEIGHT = 40

        lateinit var terminal: Terminal
        lateinit var screen: TerminalScreen
        lateinit var graphics: TextGraphics
    }
}