package pl.feureth.view.desktop.screen.borrow.list

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Borrow
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.screen.ViewModel

class BorrowListViewModel : ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(UiState(borrows = bookRepository.getBorrows()))
    val uiState: StateFlow<UiState> = _uiState

    val lazyListState = LazyListState()

    override fun onPreRender() {
        super.onPreRender()
        _uiState.value = uiState.value.copy(
            borrows = getBorrowsByQuery(query = uiState.value.searchQuery)
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.FilterSearchQueryChanged ->
                _uiState.value = uiState.value.copy(
                    searchQuery = action.query,
                    borrows = getBorrowsByQuery(uiState.value.searchQuery)
                )
        }
    }

    private fun getBorrowsByQuery(query: String) =
        bookRepository.getBorrows().filter { borrow ->
            query.isEmpty() ||
                    borrow.id.toString().contains(query, ignoreCase = true) ||
                    borrow.book.title.contains(query, ignoreCase = true) ||
                    borrow.reader.name.contains(query, ignoreCase = true) ||
                    borrow.reader.surname.contains(query, ignoreCase = true)
        }.sortedBy { it.endTime }

    data class UiState(
        val searchQuery: String = "",
        val borrows: List<Borrow> = emptyList()
    )

    sealed class Action {
        class FilterSearchQueryChanged(val query: String) : Action()
    }
}