package pl.feureth.view.desktop.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.components.HorizontalLine
import pl.feureth.view.desktop.components.TextButton
import pl.feureth.view.desktop.components.Title
import pl.feureth.view.desktop.components.VerticalLine

@Composable
fun MainMenu(
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(Constants.Padding.medium),
        horizontalArrangement = Arrangement.spacedBy(Constants.Padding.medium)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Title("Menu")
            HorizontalLine()
            Spacer(modifier = Modifier.height(Constants.Padding.small))
            MenuItem("Książki", Screen.BookList, onNavigation)
            MenuItem("Czytelnicy", Screen.ReaderList, onNavigation)
            MenuItem("Wypożyczenia", Screen.BorrowList, onNavigation)
        }
        VerticalLine()
    }
}

@Composable
fun MenuItem(
    title: String,
    screen: Screen,
    onNavigation: (Screen) -> Unit
) {
    TextButton(
        text = title,
        onClick = { onNavigation.invoke(screen) },
        modifier = Modifier.fillMaxWidth()
    )
}