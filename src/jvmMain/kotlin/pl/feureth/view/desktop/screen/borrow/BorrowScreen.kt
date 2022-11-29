package pl.feureth.view.desktop.screen.borrow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.feureth.toPrettyDate
import pl.feureth.view.desktop.components.*

@Composable
fun BorrowScreen(
    viewModel: BorrowViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Title(if (uiState.borrowId == 0L) "Nowe wypożyczenie" else "Edycja wypożyczenia")

        HorizontalLine()

        LazyColumn(
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ListSelectorField(
                    label = "Czytelnik",
                    selected = uiState.reader,
                    selectedTitle = uiState.reader?.fullName,
                    list = uiState.readers.toMutableList().apply { remove(uiState.reader) },
                    itemContent = { item, onClick ->
                        item?.let { reader ->
                            Box(
                                modifier = Modifier.fillMaxWidth().clickable { onClick.invoke(item) }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Text(reader.fullName, style = MaterialTheme.typography.button)
                            }
                        }
                    },
                    itemFilter = { item, query -> item!!.fullName.contains(query, ignoreCase = true) },
                    onSelected = { viewModel.action(BorrowViewModel.Action.ReaderChanged(it!!)) },
                    error = uiState.readerError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                ListSelectorField(
                    label = "Książka",
                    selected = uiState.book,
                    selectedTitle = uiState.book?.title,
                    list = uiState.books.toMutableList().apply { remove(uiState.book) },
                    itemContent = { item, onClick ->
                        item?.let { reader ->
                            Column(
                                modifier = Modifier.fillMaxWidth().clickable { onClick.invoke(item) }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Text(reader.title, style = MaterialTheme.typography.body1)
                                Text(reader.author, color = Color.Gray, style = MaterialTheme.typography.body2)
                            }
                        }
                    },
                    itemFilter = { item, query ->
                        item!!.title.contains(
                            query,
                            ignoreCase = true
                        ) || item.author.contains(query, ignoreCase = true)
                    },
                    onSelected = { viewModel.action(BorrowViewModel.Action.BookChanged(it!!)) },
                    error = uiState.bookError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                if (uiState.isEditMode) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        TextFieldWithLabel(
                            label = "Data rozpoczęcia wypożyczenia",
                            value = uiState.startTime ?: "",
                            onValueChange = { viewModel.action(BorrowViewModel.Action.StartTimeChanged(it)) },
                            error = uiState.startTimeError,
                            placeholder = "DD-MM-YYYY HH:MM",
                        )
                        TextButton("Teraz", onClick = {
                            viewModel.action(
                                BorrowViewModel.Action.StartTimeChanged(
                                    System.currentTimeMillis().toPrettyDate()
                                )
                            )
                        })
                    }
                } else {
                    TextWithLabel("Data rozpoczęcia wypożyczenia", uiState.startTime ?: "", modifier)
                }
            }
            item {
                if (uiState.isEditMode) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        TextFieldWithLabel(
                            label = "Data zakończenia wypożyczenia",
                            value = uiState.endTime ?: "",
                            onValueChange = { viewModel.action(BorrowViewModel.Action.EndTimeChanged(it)) },
                            error = uiState.endTimeError,
                            placeholder = "DD-MM-YYYY HH:MM",
                        )
                        TextButton("Teraz", onClick = {
                            viewModel.action(
                                BorrowViewModel.Action.EndTimeChanged(
                                    System.currentTimeMillis().toPrettyDate()
                                )
                            )
                        })
                    }
                } else {
                    TextWithLabel("Data zakończenia wypożyczenia", uiState.endTime ?: "", modifier)
                }
            }
            item {
                ActionsContainer {
                    if (uiState.isEditMode) {
                        if (uiState.borrowId == 0L) {
                            TextButtonSecondary(
                                text = "Powrót do listy",
                                onClick = { onBack.invoke() }
                            )
                        } else {
                            TextButtonSecondary(
                                text = "Anuluj",
                                onClick = { viewModel.action(BorrowViewModel.Action.EditMode(false)) }
                            )
                        }
                        TextButton(
                            text = "Zapisz",
                            onClick = {
                                if (viewModel.submit() && uiState.borrowId == 0L) {
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
                            onClick = { viewModel.action(BorrowViewModel.Action.EditMode(true)) }
                        )
                    }
                }
            }
        }
    }
}