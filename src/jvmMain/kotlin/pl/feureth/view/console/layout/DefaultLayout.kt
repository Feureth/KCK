package pl.feureth.view.console.layout

import pl.feureth.view.console.ConsoleView
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer

abstract class DefaultLayout(
    title: String,
    bottomBarText: String
) : Layout(
    title = title,
    graphics = ConsoleView.graphics,
    topBarBounds = Area(0, 0, ConsoleView.WINDOW_WIDTH, TOP_BAR_HEIGHT),
    topBarPadding = Area.fromPadding(horizontal = 3, vertical = 1),
    topBar = { drawer ->
        val bounds = drawer.innerBounds
        drawer.drawLine(0, bounds.bottom, ConsoleView.WINDOW_WIDTH, bounds.bottom, '═')
        drawer.putString(0, 0, LayoutNavigator.navigationTitle)
    },
    bottomBarBounds = Area(
        0,
        ConsoleView.WINDOW_HEIGHT - BOTTOM_BAR_HEIGHT,
        ConsoleView.WINDOW_WIDTH,
        BOTTOM_BAR_HEIGHT
    ),
    bottomBarPadding = Area.fromPadding(horizontal = 3, vertical = 1),
    bottomBar = { drawer ->
        val bounds = drawer.innerBounds
        drawer.putString(0, 2, bottomBarText)
//        bottomBar.invoke(drawer)
        drawer.drawLine(0, bounds.top, ConsoleView.WINDOW_WIDTH, bounds.top, '═')
    },
    contentBounds = Area(
        0,
        TOP_BAR_HEIGHT,
        ConsoleView.WINDOW_WIDTH,
        ConsoleView.WINDOW_HEIGHT - TOP_BAR_HEIGHT - BOTTOM_BAR_HEIGHT
    ),
    contentPadding = Area.fromPadding(horizontal = 3, vertical = 1)
) {

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun drawContent(drawer: Drawer) {
    }

    companion object {
        const val TOP_BAR_HEIGHT = 4
        const val BOTTOM_BAR_HEIGHT = 5
    }
}