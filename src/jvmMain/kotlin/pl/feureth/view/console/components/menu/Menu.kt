package pl.feureth.view.console.components.menu

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.ConsoleComponent

class Menu(
    drawer: Drawer,
    val menuList: List<MenuOption>
) : ConsoleComponent(drawer) {

    private var currentOption: Int = 0

    override fun draw() {
        menuList.forEachIndexed { index, option ->
            val isSelected = index == currentOption
            var marker = '>'
            if (isSelected) {
                drawer.setColorStyle(colorStyle = Drawer.ColorStyle.selected)
            } else {
                marker = ' '
                drawer.setColorStyle(colorStyle = Drawer.ColorStyle.default)
            }
            drawer.putString(
                0,
                index,
                " $marker " + option.title.padEnd(menuList.maxOf { it.title.length } + 3, ' ')
            )
        }
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        when (keyStroke.keyType) {
            KeyType.ArrowUp -> {
                currentOption--
                if (currentOption < 0) currentOption = menuList.size - 1
                draw()
                return true
            }

            KeyType.ArrowDown -> {
                currentOption++
                if (currentOption >= menuList.size) currentOption = 0
                draw()
                return true
            }

            KeyType.Enter -> {
                menuList[currentOption].onSelected.invoke()
                return true
            }

            else -> {}
        }
        return false
    }
}