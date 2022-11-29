package pl.feureth.view.desktop.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.components.HorizontalLine
import pl.feureth.view.desktop.components.TextButton
import pl.feureth.view.desktop.components.Title
import pl.feureth.view.desktop.components.VerticalLine

@Composable
fun MainMenu(
    currentScreen: Screen,
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
            MenuItem(
                title = "Wypożyczenia",
                screen = Screen.BorrowList,
                onNavigation = onNavigation,
                isSelected = currentScreen is Screen.BorrowDetails || currentScreen is Screen.BorrowList
            )
            MenuItem(
                title = "Książki",
                screen = Screen.BookList,
                onNavigation = onNavigation,
                isSelected = currentScreen is Screen.BookDetails || currentScreen is Screen.BookList
            )
            MenuItem(
                title = "Czytelnicy",
                screen = Screen.ReaderList,
                onNavigation = onNavigation,
                isSelected = currentScreen is Screen.ReaderDetails || currentScreen is Screen.ReaderList
            )
        }
        VerticalLine()
    }
}

@Composable
fun MenuItem(
    title: String,
    screen: Screen,
    onNavigation: (Screen) -> Unit,
    isSelected: Boolean,
) {

    val selectedPaddingStart by animateDpAsState(
        targetValue = if (isSelected) 0.dp else 24.dp,
        animationSpec = tween(
            durationMillis = 500
        )
    )

    TextButton(
        text = title,
        onClick = { onNavigation.invoke(screen) },
        modifier = Modifier.fillMaxWidth().padding(start = selectedPaddingStart),
    )
}