package pl.feureth.view.desktop.screen.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.view.desktop.components.*

@Composable
fun BookPreviewScreen(
    viewModel: BookViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Title(if (uiState.bookId == 0L) "Nowa książka" else "Edycja książki")

        HorizontalLine()

        if (uiState.isEditMode) {
            TextFieldWithLabel(
                label = "Tytuł",
                value = uiState.title,
                onValueChange = { viewModel.action(BookViewModel.Action.TitleChanged(it)) },
                error = uiState.titleError
            )
        } else {
            TextWithLabel("Tytuł", uiState.title)
        }

        if (uiState.isEditMode) {
            TextFieldWithLabel(
                label = "Autor",
                value = uiState.author,
                onValueChange = { viewModel.action(BookViewModel.Action.AuthorChanged(it)) },
                error = uiState.authorError
            )
        } else {
            TextWithLabel("Autor", uiState.author)
        }

        CheckboxWithLabel(
            label = "Wycofana",
            checked = uiState.isWithdrawn,
            onCheckedChange = { viewModel.action(BookViewModel.Action.IsWithdrawnChanged(it)) },
            enabled = uiState.isEditMode,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (uiState.isEditMode) {
                if (uiState.bookId == 0L) {
                    TextButton(
                        text = "Powrót do listy",
                        onClick = { onBack.invoke() }
                    )
                } else {
                    TextButton(
                        text = "Anuluj",
                        onClick = { viewModel.action(BookViewModel.Action.EditMode(false)) }
                    )
                }
                TextButton(
                    text = "Zapisz",
                    onClick = {
                        viewModel.submit()
                        if (uiState.bookId == 0L) {
                            onBack.invoke()
                        }
                    }
                )
            } else {
                TextButton(
                    text = "Powrót do listy",
                    onClick = { onBack.invoke() }
                )
                TextButton(
                    text = "Edytuj",
                    onClick = { viewModel.action(BookViewModel.Action.EditMode(true)) }
                )
            }
        }
    }
}