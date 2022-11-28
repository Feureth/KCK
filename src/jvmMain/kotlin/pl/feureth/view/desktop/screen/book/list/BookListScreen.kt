package pl.feureth.view.desktop.screen.book.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.feureth.model.Book
import pl.feureth.view.desktop.components.ScrollBar
import pl.feureth.view.desktop.screen.Screen

@Composable
fun BookListScreen(
    viewModel: BookListViewModel,
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(end = 16.dp).then(modifier), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Książki", style = MaterialTheme.typography.h4, modifier = Modifier.weight(1f))
            Button(onClick = { onNavigation.invoke(Screen.BookDetails()) }) {
                Text("Dodaj")
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
        TextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.action(BookListViewModel.Action.FilterSearchQueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Wyszukaj...") }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.isWithdrawn,
                onCheckedChange = { viewModel.action(BookListViewModel.Action.FilterIsWithdrawnChanged(it)) }
            )
            Text("Wycofane", style = MaterialTheme.typography.caption)
        }

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = viewModel.lazyListState
            ) {
                items(uiState.books) {
                    BookItem(
                        book = it,
                        onPreview = { book ->
                            onNavigation.invoke(Screen.BookDetails(id = book.id))
                        }
                    )
                }
            }
            if (viewModel.lazyListState.layoutInfo.totalItemsCount > 0) {
                ScrollBar(
                    scrollState = viewModel.lazyListState,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onPreview: (Book) -> Unit) {
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
            Text(book.title, color = Color.Black, style = MaterialTheme.typography.body1)
            Text(book.author, color = Color.Gray, style = MaterialTheme.typography.body2)
        }
        Button(onClick = { onPreview.invoke(book) }) { Text("Podgląd") }
    }
}