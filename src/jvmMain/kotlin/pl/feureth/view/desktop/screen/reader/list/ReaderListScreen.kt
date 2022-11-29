package pl.feureth.view.desktop.screen.reader.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.feureth.model.Reader
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.components.*
import pl.feureth.view.desktop.screen.Screen

@Composable
fun ReaderListScreen(
    viewModel: ReaderListViewModel,
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.then(modifier)) {
        Column(
            modifier = Modifier.padding(end = Constants.Padding.medium),
            verticalArrangement = Arrangement.spacedBy(Constants.Padding.small)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Title(text = "Czytelnicy", modifier = Modifier.weight(1f))
                TextButton(
                    text = "Dodaj",
                    onClick = { onNavigation.invoke(Screen.ReaderDetails()) }
                )
            }
            HorizontalLine()
            TextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.action(ReaderListViewModel.Action.FilterSearchQueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Wyszukaj...") }
            )
            CheckboxWithLabel(
                label = "Zablokowani",
                checked = uiState.isBlocked,
                onCheckedChange = { viewModel.action(ReaderListViewModel.Action.FilterIsBlockedChanged(it)) }
            )
        }

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = viewModel.lazyListState
            ) {
                items(uiState.readers) {
                    ReaderItem(
                        reader = it,
                        onPreview = { reader ->
                            onNavigation.invoke(Screen.ReaderDetails(id = reader.id))
                        }
                    )
                }
            }
            ScrollBar(
                scrollState = viewModel.lazyListState,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
fun ReaderItem(reader: Reader, onPreview: (Reader) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(end = 16.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(reader.fullName, color = Color.Black, style = MaterialTheme.typography.body1)
        }
        Button(onClick = { onPreview.invoke(reader) }) { Text("Podgląd") }
    }
}