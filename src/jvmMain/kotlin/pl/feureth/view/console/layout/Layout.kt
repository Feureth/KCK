package pl.feureth.view.console.layout

import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer

typealias LayoutSection = (drawer: Drawer) -> Unit

abstract class Layout(
    var title: String,
    graphics: TextGraphics,
    val topBarBounds: Area,
    val topBarPadding: Area,
    val topBar: LayoutSection,
    val bottomBarBounds: Area,
    val bottomBarPadding: Area,
    val bottomBar: LayoutSection,
    val contentBounds: Area,
    val contentPadding: Area,
) {

    val topBarDrawer = Drawer(graphics, topBarBounds, topBarPadding)
    val bottomBarDrawer = Drawer(graphics, bottomBarBounds, bottomBarPadding)
    val contentDrawer = Drawer(graphics, contentBounds, contentPadding)

    fun draw() {
        topBarDrawer.setColorStyle()
        topBar.invoke(topBarDrawer)

        bottomBarDrawer.setColorStyle()
        bottomBar.invoke(bottomBarDrawer)

        contentDrawer.setColorStyle()
        drawContent(contentDrawer)
    }

    abstract fun onStart()

    abstract fun onStop()

    abstract fun drawContent(drawer: Drawer)

    /**
     * @return screen should be refreshed
     */
    abstract fun performInput(keyStroke: KeyStroke): Boolean
}
