package pl.feureth.view.console.components.tab

import pl.feureth.view.console.common.Drawer

data class TabOption(
    val title: String,
    val onSelected: (drawer: Drawer, isTabChangedManually: Boolean) -> Unit
)