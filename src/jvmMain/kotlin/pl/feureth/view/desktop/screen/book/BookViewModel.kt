package pl.feureth.view.desktop.screen.book

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.screen.ViewModel

class BookViewModel : ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun setup(bookId: Long?) {
        setup(if (bookId != null) bookRepository.getBook(bookId) else null)
    }

    private fun setup(book: Book?) {
        _uiState.value = UiState(
            bookId = book?.id ?: 0L,
            title = book?.title ?: "",
            author = book?.author ?: "",
            isWithdrawn = book?.isWithdrawn ?: false,
            isEditMode = book == null,
            borrows = if (book == null) emptyList() else bookRepository.getBorrows(book = book).sortedBy { it.endTime }
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.TitleChanged -> _uiState.value = uiState.value.copy(title = action.title)
            is Action.AuthorChanged -> _uiState.value = uiState.value.copy(author = action.author)
            is Action.IsWithdrawnChanged -> _uiState.value = uiState.value.copy(isWithdrawn = action.isWithdrawn)
            is Action.EditMode -> {
                if (action.enabled) {
                    _uiState.value = uiState.value.copy(isEditMode = true)
                } else {
                    setup(bookId = uiState.value.bookId)
                }
            }
        }
    }

    fun submit(): Boolean {
        var isValid = true
        var titleError = ""
        var authorError = ""
        if (uiState.value.title.isEmpty()) {
            isValid = false
            titleError = "Pole wymagane"
        }
        if (uiState.value.author.isEmpty()) {
            isValid = false
            authorError = "Pole wymagane"
        }

        _uiState.value = uiState.value.copy(
            titleError = titleError,
            authorError = authorError
        )

        if (isValid.not()) return false

        val editedBook = Book(
            id = uiState.value.bookId,
            title = uiState.value.title,
            author = uiState.value.author,
            isWithdrawn = uiState.value.isWithdrawn,
        )
        bookRepository.insert(editedBook)
        setup(editedBook)
        return true
    }

    data class UiState(
        val bookId: Long = 0L,
        val title: String = "",
        val titleError: String = "",
        val author: String = "",
        val authorError: String = "",
        val isWithdrawn: Boolean = false,
        val isEditMode: Boolean = false,
        val borrows: List<Borrow> = emptyList(),
    )

    sealed class Action {
        class TitleChanged(val title: String): Action()
        class AuthorChanged(val author: String): Action()
        class IsWithdrawnChanged(val isWithdrawn: Boolean): Action()
        class EditMode(val enabled: Boolean): Action()
    }
}