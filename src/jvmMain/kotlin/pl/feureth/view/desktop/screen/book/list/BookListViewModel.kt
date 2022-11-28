package pl.feureth.view.desktop.screen.book.list

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Book
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.screen.ViewModel

class BookListViewModel : ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(
        UiState(
            books = bookRepository.getBooks()
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    val lazyListState = LazyListState()

    override fun onPreRender() {
        super.onPreRender()
        _uiState.value = uiState.value.copy(
            books = bookRepository.getBooks(
                query = uiState.value.searchQuery,
                isWithdrawn = uiState.value.isWithdrawn
            )
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.FilterSearchQueryChanged ->
                _uiState.value = uiState.value.copy(
                    searchQuery = action.query,
                    books = bookRepository.getBooks(query = action.query, isWithdrawn = uiState.value.isWithdrawn)
                )

            is Action.FilterIsWithdrawnChanged ->
                _uiState.value = uiState.value.copy(
                    isWithdrawn = action.isWithdrawn,
                    books = bookRepository.getBooks(query = uiState.value.searchQuery, isWithdrawn = action.isWithdrawn)
                )
        }
    }

    data class UiState(
        val searchQuery: String = "",
        val isWithdrawn: Boolean = false,
        val books: List<Book> = emptyList()
    )

    sealed class Action {
        class FilterSearchQueryChanged(val query: String) : Action()
        class FilterIsWithdrawnChanged(val isWithdrawn: Boolean) : Action()
    }
}