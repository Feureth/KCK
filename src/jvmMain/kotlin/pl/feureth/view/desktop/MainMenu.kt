package pl.feureth.view.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainMenu(
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.background(Color.Red)) {
        Button(
            onClick = { onNavigation.invoke(Screen.BookList) }
        ) {
            Text("Lista książek")
        }
    }
}