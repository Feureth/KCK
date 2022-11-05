package pl.feureth.view.console.layout

import pl.feureth.view.console.ConsoleView

object LayoutNavigator {

    private val stack = mutableListOf<Layout>()
    val current: Layout?
        get() = stack.lastOrNull()

    val navigationTitle: String
        get() = stack.joinToString(separator = " > ") { it.title }

    fun goTo(layout: Layout) {
        stack.lastOrNull()?.onStop()
        stack.add(layout)
        layout.onStart()
        refreshCurrentScreen()
    }

    /**
     * @return false if there's no layout on the stack
     */
    fun back(): Boolean {
        stack.removeLast().onStop()
        stack.lastOrNull()?.onStart()
        if (stack.isNotEmpty()) {
            refreshCurrentScreen()
            return true
        }
        return false
    }

    private fun refreshCurrentScreen() {
        ConsoleView.screen.clear()
        current?.draw()
        ConsoleView.screen.refresh()
    }
}