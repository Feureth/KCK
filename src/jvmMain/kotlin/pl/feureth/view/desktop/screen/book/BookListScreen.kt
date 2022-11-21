package pl.feureth.view.desktop.screen.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.feureth.model.Book
import pl.feureth.presenter.book.BookRepository

@Composable
fun BookListScreen(
    bookRepository: BookRepository,
    modifier: Modifier = Modifier
) {

    var books by remember { mutableStateOf(bookRepository.getBooks()) }

    LazyColumn(modifier = modifier) {
        item {
            Text("Książki", style = MaterialTheme.typography.h6)
        }
        item {
            Button(
                onClick = {
                    bookRepository.insert(Book(id = 0L, title = "T3st", author = "Test"))
                    books = bookRepository.getBooks()
                }
            ) {
                Text("Dodaj testowo")
            }
        }
        item {
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Gray).padding(vertical = 8.dp))
        }
        items(books) {
            BookItem(it)
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
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