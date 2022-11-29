package pl.feureth.view.desktop

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import pl.feureth.view.AppView
import pl.feureth.view.desktop.screen.MainMenu
import pl.feureth.view.desktop.screen.Screen
import pl.feureth.view.desktop.screen.ScreenContent
import java.awt.Dimension

class DesktopView : AppView {

    override fun start() = application {
        val windowState = rememberWindowState(width = 1024.dp, height = 768.dp).apply {
            placement = WindowPlacement.Maximized
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Biblioteka",
            state = windowState
        ) {
            LaunchedEffect(Unit) {
                window.minimumSize = Dimension(1024, 768)
            }
            MaterialTheme {
                MainContainer()
            }
        }
    }
}

@Composable
fun MainContainer() {

    var currentScreen by remember { mutableStateOf<Screen>(Screen.BorrowList) }

    Row(modifier = Modifier.fillMaxSize()) {
        MainMenu(
            onNavigation = { currentScreen = it },
            modifier = Modifier.width(250.dp).fillMaxHeight()
        )
        ScreenContent(
            screen = currentScreen,
            onNavigation = { currentScreen = it },
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}