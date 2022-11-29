package pl.feureth.view.desktop.screen.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.feureth.view.desktop.components.*

@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Title(if (uiState.readerId == 0L) "Nowy czytelnik" else "Edycja czytelnika")

        HorizontalLine()

        TextFieldWithEditMode(
            label = "Imię",
            value = uiState.name,
            onValueChange = { viewModel.action(ReaderViewModel.Action.NameChanged(it)) },
            error = uiState.nameError,
            isEditMode = uiState.isEditMode
        )
        TextFieldWithEditMode(
            label = "Nazwisko",
            value = uiState.surname,
            onValueChange = { viewModel.action(ReaderViewModel.Action.SurnameChanged(it)) },
            error = uiState.surnameError,
            isEditMode = uiState.isEditMode
        )

        CheckboxWithLabel(
            label = "Zablokowana/y",
            checked = uiState.isBlocked,
            onCheckedChange = { viewModel.action(ReaderViewModel.Action.IsBlockedChanged(it)) },
            enabled = uiState.isEditMode,
        )

        ActionsContainer {
            if (uiState.isEditMode) {
                if (uiState.readerId == 0L) {
                    TextButtonSecondary(
                        text = "Powrót do listy",
                        onClick = { onBack.invoke() }
                    )
                } else {
                    TextButtonSecondary(
                        text = "Anuluj",
                        onClick = { viewModel.action(ReaderViewModel.Action.EditMode(false)) }
                    )
                }
                TextButton(
                    text = "Zapisz",
                    onClick = {
                        viewModel.submit()
                        if (uiState.readerId == 0L) {
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
                    onClick = { viewModel.action(ReaderViewModel.Action.EditMode(true)) }
                )
            }
        }

        if (uiState.isEditMode.not()) {
            Title("Wypożyczenia")
            LazyColumn {
                items(uiState.borrows) {

                }
            }
        }
    }
}