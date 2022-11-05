package pl.feureth.view.console.common

data class Area(var x: Int, var y: Int, var width: Int, var height: Int) {

    val left: Int
        get() = x
    val top: Int
        get() = y
    val right: Int
        get() = x + width
    val bottom: Int
        get() = y + height

    fun withPadding(padding: Area): Area {
        return fromPadding(
            top = this.top + padding.top,
            left = this.left + padding.left,
            bottom = this.bottom - padding.bottom,
            right = this.right - padding.right
        )
    }

    companion object {

        val zero: Area = Area(0, 0, 0, 0)

        fun fromPadding(top: Int = 0, right: Int = 0, bottom: Int = 0, left: Int = 0): Area =
            Area(x = left, y = top, width = right - left, height = bottom - top)

        fun fromPadding(vertical: Int = 0, horizontal: Int = 0): Area =
            fromPadding(top = vertical, right = horizontal, bottom = vertical, left = horizontal)
    }
}