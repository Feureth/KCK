package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer

class Text(
    drawer: Drawer,
    var text: String,
    isSelected: Boolean = false,
    isSelectable: Boolean = false,
    private val colorStyle: Drawer.ColorStyle = Drawer.ColorStyle.default
) : FormField(drawer, isSelected, isSelectable) {

    override fun draw() {
        super.draw()
        drawer.setColorStyle(colorStyle)
        drawer.putString(markerWidth, 0, text)
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return keyStroke.keyType == KeyType.Enter
    }
}