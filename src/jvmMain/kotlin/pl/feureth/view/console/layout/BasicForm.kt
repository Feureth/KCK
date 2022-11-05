package pl.feureth.view.console.layout

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.ConsoleComponent
import pl.feureth.view.console.components.FormField

class BasicForm(
    drawer: Drawer,
    val fields: List<FormField>,
    val onFieldEnter: (FormField) -> Unit
) : ConsoleComponent(drawer) {

    private var currentOption: Int = 0

    override fun draw() {
        fields.forEach { formField ->
            formField.draw()
        }
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        when (keyStroke.keyType) {
            KeyType.ArrowUp -> {
                fields[currentOption].isSelected = false
                currentOption--
                if (currentOption < 0) currentOption = fields.size - 1
                fields[currentOption].isSelected = true
                draw()
                return true
            }

            KeyType.ArrowDown -> {
                fields[currentOption].isSelected = false
                currentOption++
                if (currentOption >= fields.size) currentOption = 0
                fields[currentOption].isSelected = true
                draw()
                return true
            }

            KeyType.Enter -> {
                onFieldEnter.invoke(fields[currentOption])
                return true
            }

            else -> {
                return fields[currentOption].performInput(keyStroke)
            }
        }
    }
}