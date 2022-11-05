package pl.feureth.view.console.components.table

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import pl.feureth.view.console.common.Drawer
import kotlin.math.min

abstract class Table<T>(
    val drawer: Drawer,
    val columnList: List<TableColumn>
) {

    private val columnWidths = mutableListOf<Int>()
    private val data = mutableListOf<T>()

    private val maxTableRowCount = drawer.innerBounds.height - HEADER_HEIGHT - 1

    private var firstVisibleIndex = 0
    private var lastVisibleIndex = 0
    var currentSelectedIndex = 0

    abstract fun convert(item: T): List<String>

    abstract fun onItemClick(item: T)

    init {
        val weightSum = columnList.sumOf { it.weight }
        val contentWidth = drawer.innerBounds.width
        var currentX = 0
        columnList.forEachIndexed { index, column ->
            val isLastColumn = index == columnList.size - 1
            val columnWidth = if (isLastColumn) {
                contentWidth - currentX
            } else ((column.weight / weightSum.toFloat()) * contentWidth).toInt()
            columnWidths.add(columnWidth)
            currentX += columnWidth
        }
    }

    fun setData(data: List<T>, resetPointer: Boolean) {
        this.data.clear()
        this.data.addAll(data)
        firstVisibleIndex = 0
        lastVisibleIndex = min(data.size - 1, maxTableRowCount)
        if (resetPointer || currentSelectedIndex !in firstVisibleIndex..lastVisibleIndex) {
            currentSelectedIndex = 0
        }
        draw()
    }

    fun performInput(keyStroke: KeyStroke) {
        when (keyStroke.keyType) {
            KeyType.ArrowUp -> {
                currentSelectedIndex--
                if (currentSelectedIndex < 0) {
                    currentSelectedIndex = data.size - 1
                    lastVisibleIndex = currentSelectedIndex
                    firstVisibleIndex = currentSelectedIndex - min(data.size - 1, maxTableRowCount)
                } else if (currentSelectedIndex < firstVisibleIndex) {
                    firstVisibleIndex = currentSelectedIndex
                    lastVisibleIndex = firstVisibleIndex + min(data.size - 1, maxTableRowCount)
                }
                draw()
            }
            KeyType.ArrowDown -> {
                currentSelectedIndex++
                if (currentSelectedIndex >= data.size) {
                    currentSelectedIndex = 0
                    firstVisibleIndex = 0
                    lastVisibleIndex = min(data.size - 1, maxTableRowCount)
                } else if (currentSelectedIndex > lastVisibleIndex) {
                    lastVisibleIndex = currentSelectedIndex
                    firstVisibleIndex = lastVisibleIndex - min(data.size - 1, maxTableRowCount)
                }
                draw()
            }
            KeyType.Enter -> {
                if (data.isNotEmpty())
                    onItemClick(data[currentSelectedIndex])
            }
            else -> {}
        }
    }

    fun draw() {
        drawer.clearContent()
        drawHeader()
        for (index in firstVisibleIndex..lastVisibleIndex) {
            drawDataRow(convert(data[index]), index, index - firstVisibleIndex)
        }
    }

    private fun drawHeader() {
        drawer.setColorStyle(Drawer.ColorStyle.TableBorder)
        drawer.drawLine(
            drawer.innerBounds.left,
            drawer.innerBounds.top,
            drawer.innerBounds.right,
            drawer.innerBounds.top,
            '═'
        )
        drawRow(columnList.map { it.title }, 1, isSelected = false, isHeader = true)
        drawer.setColorStyle(Drawer.ColorStyle.TableBorder)
        drawer.drawLine(
            drawer.innerBounds.left,
            drawer.innerBounds.top + 2,
            drawer.innerBounds.right,
            drawer.innerBounds.top + 2,
            '═'
        )
    }

    private fun drawDataRow(values: List<String>, index: Int, offsetY: Int) {
        if (values.size != columnWidths.size) throw IllegalArgumentException()
        drawRow(values, HEADER_HEIGHT + offsetY, index == currentSelectedIndex, isHeader = false)
    }

    private fun drawRow(values: List<String>, offsetY: Int, isSelected: Boolean, isHeader: Boolean) {
        if (values.size != columnWidths.size) throw IllegalArgumentException()
        var currentX = 0
        columnList.forEachIndexed { index, column ->
            val isLastColumn = index == columnList.size - 1
            val columnWidth = columnWidths[index]

            var strTitle = "║ ${values[index]}".padEnd(columnWidth)
            if (isLastColumn) {
                strTitle = strTitle.substring(0, strTitle.length) + "║"
            }

            when {
                isSelected->drawer.setColorStyle(Drawer.ColorStyle.TableSelectedItem)
                isHeader->drawer.setColorStyle(Drawer.ColorStyle.TableBorder)
                else ->drawer.setColorStyle()
            }
            drawer.putString(currentX, offsetY, strTitle)
            currentX += columnWidth
        }
    }

    companion object {
        const val HEADER_HEIGHT = 3
    }
}