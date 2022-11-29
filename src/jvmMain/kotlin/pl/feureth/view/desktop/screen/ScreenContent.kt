package pl.feureth.view.desktop.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.feureth.view.desktop.Constants
import pl.feureth.view.desktop.screen.book.BookPreviewScreen
import pl.feureth.view.desktop.screen.book.BookViewModel
import pl.feureth.view.desktop.screen.book.list.BookListScreen
import pl.feureth.view.desktop.screen.book.list.BookListViewModel
import pl.feureth.view.desktop.screen.borrow.BorrowScreen
import pl.feureth.view.desktop.screen.borrow.BorrowViewModel
import pl.feureth.view.desktop.screen.borrow.list.BorrowListScreen
import pl.feureth.view.desktop.screen.borrow.list.BorrowListViewModel
import pl.feureth.view.desktop.screen.reader.ReaderScreen
import pl.feureth.view.desktop.screen.reader.ReaderViewModel
import pl.feureth.view.desktop.screen.reader.list.ReaderListScreen
import pl.feureth.view.desktop.screen.reader.list.ReaderListViewModel

@Composable
fun ScreenContent(
    screen: Screen,
    onNavigation: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.padding(top = Constants.Padding.medium)) {
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
                    onBorrowPreview = { onNavigation.invoke(Screen.BorrowDetails(it.id)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Screen.ReaderList -> {
                val viewModel = ViewModelProvider.provide(ReaderListViewModel::class.java).apply {
                    onPreRender()
                }
                ReaderListScreen(
                    viewModel = viewModel,
                    onNavigation = onNavigation,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is Screen.ReaderDetails -> {
                val viewModel = ViewModelProvider.provide(ReaderViewModel::class.java).apply {
                    setup(screen.id)
                    onPreRender()
                }
                ReaderScreen(
                    viewModel = viewModel,
                    onBack = { onNavigation.invoke(Screen.ReaderList) },
                    onBorrowPreview = { onNavigation.invoke(Screen.BorrowDetails(it.id)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Screen.BorrowList -> {
                val viewModel = ViewModelProvider.provide(BorrowListViewModel::class.java).apply {
                    onPreRender()
                }
                BorrowListScreen(
                    viewModel = viewModel,
                    onNavigation = onNavigation,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is Screen.BorrowDetails -> {
                val viewModel = ViewModelProvider.provide(BorrowViewModel::class.java).apply {
                    setup(screen.id)
                    onPreRender()
                }
                BorrowScreen(
                    viewModel = viewModel,
                    onBack = { onNavigation.invoke(Screen.BorrowList) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}