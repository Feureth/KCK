package pl.feureth.view.console.components

import pl.feureth.view.console.common.Drawer

abstract class FormField(drawer: Drawer, var isSelected: Boolean = false, var isSelectable: Boolean) : ConsoleComponent(drawer) {

    val markerWidth = 4

    override fun draw() {
        drawer.setColorStyle()
        if (isSelectable) {
            if (isSelected) {
                drawer.setColorStyle(Drawer.ColorStyle.selected)
            }
            val marker = if (isSelected) '>' else ' '
            drawer.putString(0, 0, " $marker ")
        }
    }
}