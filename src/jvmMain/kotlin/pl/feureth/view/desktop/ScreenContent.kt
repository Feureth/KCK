package pl.feureth.view.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScreenContent(
    screen: Screen,
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(screen.toString())
    }
}