package pl.feureth.view.console.components.menu

data class MenuOption(
    val title: String,
    val onSelected: () -> Unit
)