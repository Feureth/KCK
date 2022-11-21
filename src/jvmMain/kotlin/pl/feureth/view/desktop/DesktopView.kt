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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.AppView
import pl.feureth.view.desktop.screen.MainMenu
import pl.feureth.view.desktop.screen.Screen
import pl.feureth.view.desktop.screen.ScreenContent
import java.awt.Dimension

class DesktopView : AppView, KoinComponent {

    private val bookRepository: BookRepository by inject()

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
                window.minimumSize = Dimension(800, 600)
            }
            MaterialTheme {
                MainContainer(bookRepository)
            }
        }
    }
}

@Composable
fun MainContainer(bookRepository: BookRepository) {

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Start) }

    Row(modifier = Modifier.fillMaxSize()) {
        MainMenu(
            onNavigation = { currentScreen = it },
            modifier = Modifier.width(250.dp).fillMaxHeight()
        )
        ScreenContent(
            bookRepository = bookRepository,
            screen = currentScreen,
            onNavigation = { currentScreen = it },
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}