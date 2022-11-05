package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import pl.feureth.view.console.common.Drawer

abstract class ConsoleComponent(
    val drawer: Drawer
) {

    abstract fun draw()

    abstract fun performInput(keyStroke: KeyStroke): Boolean

}