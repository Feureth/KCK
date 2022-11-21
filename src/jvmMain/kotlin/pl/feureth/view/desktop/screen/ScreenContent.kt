package pl.feureth.view.desktop.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.screen.book.BookListScreen

@Composable
fun ScreenContent(
    bookRepository: BookRepository,
    screen: Screen,
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .padding(top = Constants.Padding.medium)
    ) {
        when (screen) {
            Screen.Start -> {
            }

            Screen.BookList -> {
                BookListScreen(
                    bookRepository = bookRepository,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is Screen.BookDetails -> TODO()
            is Screen.BookEditor -> TODO()

            Screen.ReaderList -> {
            }

            is Screen.ReaderDetails -> TODO()
            is Screen.ReaderEditor -> TODO()

            Screen.BorrowList -> {
            }

            is Screen.BorrowDetails -> TODO()
            is Screen.BorrowEditor -> TODO()
        }
    }
}