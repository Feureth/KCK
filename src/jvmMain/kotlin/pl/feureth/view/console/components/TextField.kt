package pl.feureth.view.console.components

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer

class TextField(
    drawer: Drawer,
    val label: String,
    val placeholder: String,
    val onTextChanged: (text: String) -> Unit,
    isSelected: Boolean = false,
    isSelectable: Boolean = false,
    val isEditable: Boolean = true,
) : FormField(drawer, isSelected, isSelectable) {

    var text: String = ""

    override fun draw() {
        super.draw()
        drawer.setColorStyle()
        var headerWidth = markerWidth
        if (label.isNotEmpty()) {
            drawer.putString(markerWidth, 0, label)
            headerWidth += label.length
        }
        val content = if (text.isEmpty()) {
            drawer.setColorStyle(Drawer.ColorStyle.DisabledTextField)
            placeholder.padEnd(50)
        } else {
            drawer.setColorStyle(Drawer.ColorStyle.ActiveTextField)
            text.padEnd(50)
        }
        drawer.putString(headerWidth + 2, 0, content)
    }

    /**
     * @return is keyboard consumed
     */
    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (isEditable.not()) return false
        return when (keyStroke.keyType) {
            KeyType.Backspace -> {
                if (text.isNotEmpty()) {
                    text = text.substring(0, text.length - 1)
                    draw()
                    onTextChanged.invoke(text)
                    true
                } else {
                    false
                }
            }

            KeyType.Character -> {
                text += keyStroke.character
                onTextChanged.invoke(text)
                draw()
                true
            }

            else -> false
        }
    }
}