package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer

class CheckBox(
    drawer: Drawer,
    var label: String,
    var isChecked: Boolean,
    isSelected: Boolean = false,
    isSelectable: Boolean = false
) : FormField(drawer, isSelected, isSelectable) {

    override fun draw() {
        super.draw()
        drawer.setColorStyle()
        val strCheckedState = if (isChecked) "[X]" else "[ ]"
        drawer.putString(markerWidth, 0, "$strCheckedState $label")
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (keyStroke.keyType == KeyType.Enter || keyStroke.character == ' ') {
            isChecked = isChecked.not()
            draw()
            return true
        }
        return false
    }
}