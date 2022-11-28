package pl.feureth.view.desktop.screen.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookPreviewScreen(
    viewModel: BookViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            text = if (uiState.bookId == 0L) "Nowa książka" else "Edycja książki",
            style = MaterialTheme.typography.h4
        )

        Column {
            Text("Tytuł")
            if (uiState.isEditMode) {
                TextField(
                    value = uiState.title,
                    onValueChange = { viewModel.action(BookViewModel.Action.TitleChanged(it)) },
                    isError = uiState.titleError.isNotEmpty(),
                )
                if (uiState.titleError.isNotEmpty()) {
                    Text(uiState.titleError, style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.error)
                }
            } else {
                Text(uiState.title, style = MaterialTheme.typography.h5)
            }
        }

        Column {
            Text("Autor")
            if (uiState.isEditMode) {
                TextField(
                    value = uiState.author,
                    onValueChange = { viewModel.action(BookViewModel.Action.AuthorChanged(it)) },
                    isError = uiState.authorError.isNotEmpty(),
                )
                if (uiState.authorError.isNotEmpty()) {
                    Text(uiState.authorError, style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.error)
                }
            } else {
                Text(uiState.author, style = MaterialTheme.typography.h5)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.isWithdrawn,
                onCheckedChange = { viewModel.action(BookViewModel.Action.IsWithdrawnChanged(it)) },
                enabled = uiState.isEditMode,
            )
            Text("Wycofana", style = MaterialTheme.typography.caption)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (uiState.isEditMode) {
                if (uiState.bookId == 0L) {
                    Button(onClick = { onBack.invoke() }) {
                        Text("Powrót do listy")
                    }
                } else {
                    Button(onClick = { viewModel.action(BookViewModel.Action.EditMode(false)) }) {
                        Text("Anuluj")
                    }
                }
                Button(onClick = {
                    viewModel.submit()
                    if (uiState.bookId == 0L) {
                        onBack.invoke()
                    }
                }) {
                    Text("Zapisz")
                }
            } else {
                Button(onClick = { onBack.invoke() }) {
                    Text("Powrót do listy")
                }
                Button(onClick = { viewModel.action(BookViewModel.Action.EditMode(true)) }) {
                    Text("Edytuj")
                }
            }
        }
    }
}