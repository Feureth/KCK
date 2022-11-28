package pl.feureth.view.desktop.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.screen.book.BookPreviewScreen
import pl.feureth.view.desktop.screen.book.list.BookListScreen
import pl.feureth.view.desktop.screen.book.list.BookListViewModel
import pl.feureth.view.desktop.screen.book.BookViewModel

@Composable
fun ScreenContent(
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
                val viewModel = ViewModelProvider.provide(BookListViewModel::class.java).apply {
                    onPreRender()
                }
                BookListScreen(
                    viewModel = viewModel,
                    onNavigation = onNavigation,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is Screen.BookDetails -> {
                val viewModel = ViewModelProvider.provide(BookViewModel::class.java).apply {
                    setup(screen.id)
                    onPreRender()
                }
                BookPreviewScreen(
                    viewModel = viewModel,
                    onBack = { onNavigation.invoke(Screen.BookList) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Screen.ReaderList -> {
            }

            is Screen.ReaderDetails -> TODO()

            Screen.BorrowList -> {
            }

            is Screen.BorrowDetails -> TODO()
        }
    }
}