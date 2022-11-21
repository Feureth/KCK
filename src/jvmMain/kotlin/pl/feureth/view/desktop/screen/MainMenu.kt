package pl.feureth.view.desktop.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.view.desktop.Constants

@Composable
fun MainMenu(
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(Constants.Padding.medium)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Menu", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Constants.Color.Spacer))
            Spacer(modifier = Modifier.height(Constants.Padding.small))
            MenuItem("Książki", Screen.BookList, onNavigation)
            MenuItem("Czytelnicy", Screen.ReaderList, onNavigation)
            MenuItem("Wypożyczenia", Screen.BorrowList, onNavigation)
        }
        Spacer(modifier = Modifier.width(Constants.Padding.medium))
        Spacer(modifier = Modifier.width(1.dp).fillMaxHeight().background(Constants.Color.Spacer))
    }
}

@Composable
fun MenuItem(
    title: String,
    screen: Screen,
    onNavigation: (Screen) -> Unit
) {
    Button(
        onClick = { onNavigation.invoke(screen) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title)
    }
}