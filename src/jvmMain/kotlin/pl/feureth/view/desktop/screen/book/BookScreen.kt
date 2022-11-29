package pl.feureth.view.desktop.screen.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.model.Borrow
import pl.feureth.view.desktop.components.*
import pl.feureth.view.desktop.screen.borrow.list.BorrowItem

@Composable
fun BookPreviewScreen(
    viewModel: BookViewModel,
    onBack: () -> Unit,
    onBorrowPreview: (Borrow) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Title(if (uiState.bookId == 0L) "Nowa książka" else "Edycja książki")

        HorizontalLine()

        TextFieldWithEditMode(
            label = "Tytuł",
            value = uiState.title,
            onValueChange = { viewModel.action(BookViewModel.Action.TitleChanged(it)) },
            error = uiState.titleError,
            isEditMode = uiState.isEditMode
        )
        TextFieldWithEditMode(
            label = "Autor",
            value = uiState.author,
            onValueChange = { viewModel.action(BookViewModel.Action.AuthorChanged(it)) },
            error = uiState.authorError,
            isEditMode = uiState.isEditMode
        )

        CheckboxWithLabel(
            label = "Wycofana",
            checked = uiState.isWithdrawn,
            onCheckedChange = { viewModel.action(BookViewModel.Action.IsWithdrawnChanged(it)) },
            enabled = uiState.isEditMode,
        )

        ActionsContainer {
            if (uiState.isEditMode) {
                if (uiState.bookId == 0L) {
                    TextButtonSecondary(
                        text = "Powrót do listy",
                        onClick = { onBack.invoke() }
                    )
                } else {
                    TextButtonSecondary(
                        text = "Anuluj",
                        onClick = { viewModel.action(BookViewModel.Action.EditMode(false)) }
                    )
                }
                TextButton(
                    text = "Zapisz",
                    onClick = {
                        if (viewModel.submit() && uiState.bookId == 0L) {
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

        if (uiState.isEditMode.not()) {
            Title("Wypożyczenia")
            LazyColumn {
                items(uiState.borrows) {
                    BorrowItem(it, onPreview = onBorrowPreview)
                }
            }
        }
    }
}