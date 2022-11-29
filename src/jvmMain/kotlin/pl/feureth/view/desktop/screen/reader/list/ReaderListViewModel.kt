package pl.feureth.view.desktop.screen.reader.list

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.screen.ViewModel

class ReaderListViewModel: ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(UiState(readers = bookRepository.getReaders()))
    val uiState: StateFlow<UiState> = _uiState

    val lazyListState = LazyListState()

    override fun onPreRender() {
        super.onPreRender()
        _uiState.value = uiState.value.copy(
            readers = bookRepository.getReaders(
                query = uiState.value.searchQuery,
                isBlocked = uiState.value.isBlocked
            )
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.FilterSearchQueryChanged ->
                _uiState.value = uiState.value.copy(
                    searchQuery = action.query,
                    readers = bookRepository.getReaders(query = action.query, isBlocked = uiState.value.isBlocked)
                )

            is Action.FilterIsBlockedChanged ->
                _uiState.value = uiState.value.copy(
                    isBlocked = action.isBlocked,
                    readers = bookRepository.getReaders(query = uiState.value.searchQuery, isBlocked = action.isBlocked)
                )
        }
    }

    data class UiState(
        val searchQuery: String = "",
        val isBlocked: Boolean = false,
        val readers: List<Reader> = emptyList()
    )

    sealed class Action {
        class FilterSearchQueryChanged(val query: String) : Action()
        class FilterIsBlockedChanged(val isBlocked: Boolean) : Action()
    }
}