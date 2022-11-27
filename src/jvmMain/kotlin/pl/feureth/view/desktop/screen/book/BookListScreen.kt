package pl.feureth.view.desktop.screen.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.feureth.model.Book
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.components.ScrollBar

@Composable
fun BookListScreen(
    bookRepository: BookRepository,
    modifier: Modifier = Modifier
) {

    val books by remember { mutableStateOf(bookRepository.getBooks()) }
    val listScrollState = rememberLazyListState()
    var strSearchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text("Książki", style = MaterialTheme.typography.h3)
        TextField(
            value = strSearchQuery,
            onValueChange = { strSearchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Wyszukaj...") }
        )
        Spacer(
            modifier = Modifier.fillMaxWidth().height(1.dp).padding(end = 16.dp).background(Color.Gray)
                .padding(vertical = 8.dp)
        )

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = listScrollState
            ) {
                items(books.filter {
                    it.title.contains(strSearchQuery, ignoreCase = true) ||
                            it.author.contains(strSearchQuery, ignoreCase = true)
                }) {
                    BookItem(it)
                }
            }
            if (listScrollState.layoutInfo.totalItemsCount > 0) {
                ScrollBar(
                    scrollState = listScrollState,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun BookItem(book: Book) {
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
        Row {
            Button(onClick = {}) { Text("Edytuj") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {}) { Text("Podgląd") }
        }
    }
}