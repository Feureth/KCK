package pl.feureth.view.console.components.tab

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer

class TabContainer(
    val drawer: Drawer,
    val menuOptions: List<TabOption>
) {

    private var currentOption: Int = 0

    val contentDrawer = Drawer(
        drawer.graphics,
        drawer.innerBounds,
        Area.fromPadding(top = 4)
    )

    fun draw(isTabChangedManually: Boolean = false) {
        val optionWidth = drawer.innerBounds.width / menuOptions.size
        menuOptions.forEachIndexed { index, option ->
            val isSelected = index == currentOption
            if (isSelected) {
                drawer.setColorStyle(colorStyle = Drawer.ColorStyle.TabSelected)
            } else {
                drawer.setColorStyle(colorStyle = Drawer.ColorStyle.TabUnselected)
            }
            val strTitle = ("   " + option.title).padEnd(optionWidth, ' ')
            drawer.putString(optionWidth * index, 0, strTitle)
        }
        drawer.setColorStyle()
        menuOptions[currentOption].onSelected.invoke(contentDrawer, isTabChangedManually)
    }

    fun performInput(keyStroke: KeyStroke): Boolean {
        return when (keyStroke.keyType) {
            KeyType.ArrowLeft -> {
                currentOption--
                if (currentOption < 0) currentOption = menuOptions.size - 1
                draw(isTabChangedManually = true)
                true
            }

            KeyType.ArrowRight -> {
                currentOption++
                if (currentOption >= menuOptions.size) currentOption = 0
                draw(isTabChangedManually = true)
                true
            }

            else -> {
                false
            }
        }
    }
}