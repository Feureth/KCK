package pl.feureth.view.desktop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import pl.feureth.view.AppView

class DesktopView : AppView {

    override fun start() = application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Biblioteka",
            state = rememberWindowState(width = 300.dp, height = 300.dp)
        ) {
            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize()){
                    Text("co tam?")
                    Text("a nie wiem")
                }
            }
        }
    }
}