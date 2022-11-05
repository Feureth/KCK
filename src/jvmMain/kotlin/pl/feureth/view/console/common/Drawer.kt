package pl.feureth.view.console.common

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.TextGraphics

data class Drawer(
    val graphics: TextGraphics,
    val bounds: Area,
    val padding: Area
) {

    val innerBounds: Area = bounds.withPadding(padding)

    fun clearContent() {
        setColorStyle()
        graphics.fillRectangle(
            TerminalPosition(innerBounds.x, innerBounds.y),
            TerminalSize(innerBounds.width + 1, innerBounds.height),
            ' '
        )
    }

    fun setColor(color: TextColor) {
        graphics.foregroundColor = color
    }

    fun setBackgroundColor(color: TextColor) {
        graphics.backgroundColor = color
    }

    fun setColorStyle(colorStyle: ColorStyle = ColorStyle.default) {
        setColor(colorStyle.foreground)
        setBackgroundColor(colorStyle.background)
    }

    fun putString(x: Int, y: Int, text: String) {
        graphics.putString(innerBounds.x + x, innerBounds.y + y, text)
    }

    fun drawLine(startX: Int, startY: Int, endX: Int, endY: Int, character: Char = '=') {
        graphics.drawLine(startX, startY, endX, endY, character)
    }

    fun drawInfo(drawer: Drawer, y: Int, label: String, value: String, isError: Boolean = false) {
        if (isError) {
            drawer.setColorStyle(Drawer.ColorStyle.error)
        } else {
            drawer.setColorStyle()
        }
        drawer.putString(0, y, label.padEnd(30, ' ') + value)
        drawer.setColorStyle()
    }

    fun innerDrawer(padding: Area): Drawer {
        return Drawer(graphics, innerBounds, padding)
    }

    enum class ColorStyle(val foreground: TextColor, val background: TextColor) {
        WhiteOnBlack(foreground = TextColor.ANSI.WHITE, background = TextColor.ANSI.BLACK),
        BlackOnWhite(foreground = TextColor.ANSI.BLACK, background = TextColor.ANSI.WHITE),
        Disabled(foreground = TextColor.RGB(120, 120, 120), background = TextColor.ANSI.BLACK),

        WhiteOnBlue(foreground = TextColor.ANSI.WHITE, background = TextColor.ANSI.BLUE),
        RedOnBlack(foreground = TextColor.ANSI.RED_BRIGHT, background = TextColor.ANSI.BLACK),
        YellowOnBlack(foreground = TextColor.ANSI.YELLOW_BRIGHT, background = TextColor.ANSI.BLACK),

        TabSelected(foreground = TextColor.ANSI.BLACK, background = TextColor.ANSI.WHITE),
        TabUnselected(foreground = TextColor.ANSI.WHITE, background = TextColor.ANSI.BLACK),
//        TabSelected(foreground = TextColor.ANSI.BLACK, background = TextColor.ANSI.YELLOW_BRIGHT),
//        TabUnselected(foreground = TextColor.ANSI.WHITE, background = TextColor.RGB(255, 255, 150)),
//        TabSelected(foreground = TextColor.ANSI.BLACK, background = TextColor.RGB(90, 186, 242)),
//        TabUnselected(foreground = TextColor.ANSI.BLACK, background = TextColor.RGB(126, 241, 247)),

        TableBorder(foreground = TextColor.ANSI.YELLOW_BRIGHT, background = TextColor.ANSI.BLACK),
        TableSelectedItem(foreground = TextColor.ANSI.BLACK, background = TextColor.ANSI.WHITE),

//        disabledTextField(foreground = TextColor.RGB(50, 50, 50), background = TextColor.ANSI.WHITE),
//        activeTextField(foreground = TextColor.ANSI.BLACK, background = TextColor.ANSI.WHITE),
        DisabledTextField(foreground = TextColor.ANSI.BLACK, background = TextColor.RGB(120, 120, 50)),
        ActiveTextField(foreground = TextColor.ANSI.BLACK, background = TextColor.RGB(255, 255, 85)),

        Logo(foreground = TextColor.ANSI.GREEN, background = TextColor.ANSI.BLACK);

        companion object {
            val default: ColorStyle = WhiteOnBlack
            val selected: ColorStyle = BlackOnWhite
            val disabled: ColorStyle = Disabled
            val error: ColorStyle = RedOnBlack
        }
    }
}