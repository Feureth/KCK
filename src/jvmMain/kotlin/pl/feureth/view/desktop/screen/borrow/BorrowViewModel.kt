package pl.feureth.view.desktop.screen.borrow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.defaultSdf
import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.desktop.screen.ViewModel
import java.text.ParseException

class BorrowViewModel : ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun setup(borrowId: Long?) {
        setup(if (borrowId != null) bookRepository.getBorrow(borrowId) else null)
    }

    private fun setup(borrow: Borrow?) {
        _uiState.value = UiState(
            borrowId = borrow?.id ?: 0L,
            reader = borrow?.reader,
            readers = bookRepository.getReaders(isBlocked = false),
            book = borrow?.book,
            bookError = "",
            books = bookRepository.getBooks(isWithdrawn = false, isBorrowed = false),
            startTime = borrow?.startTime?.toPrettyDate(),
            endTime = borrow?.endTime?.toPrettyDate(),
            isEditMode = borrow == null,
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.ReaderChanged -> _uiState.value = uiState.value.copy(reader = action.reader)
            is Action.BookChanged -> _uiState.value = uiState.value.copy(book = action.book)
            is Action.StartTimeChanged -> _uiState.value = uiState.value.copy(startTime = action.startTime)
            is Action.EndTimeChanged -> _uiState.value = uiState.value.copy(endTime = action.endTime)
            is Action.EditMode -> {
                if (action.enabled) {
                    _uiState.value = uiState.value.copy(isEditMode = true)
                } else {
                    setup(borrowId = uiState.value.borrowId)
                }
            }
        }
    }

    fun submit(): Boolean {

        val startTime: Long? = try {
            if (uiState.value.startTime.isNullOrEmpty()) null
            else defaultSdf.parse(uiState.value.startTime).time
        } catch (e: ParseException) {
            null
        }
        val endTime: Long? = try {
            if (uiState.value.endTime.isNullOrEmpty()) null
            else defaultSdf.parse(uiState.value.endTime).time
        } catch (e: ParseException) {
            null
        }

        var isValid = true

        var readerError = ""
        var bookError = ""
        var startTimeError = ""
        var endTimeError = ""

        if (uiState.value.reader == null) {
            isValid = false
            readerError = "Pole wymagane"
        }
        if (uiState.value.book == null) {
            isValid = false
            bookError = "Pole wymagane"
        }
        if (uiState.value.startTime.isNullOrEmpty()) {
            isValid = false
            startTimeError = "Pole wymagane"
        } else if (startTime == null) {
            isValid = false
            startTimeError = "Nieprawidłowy format, wymagane: DD-MM-YYYY HH:MM"
        }
        if (uiState.value.endTime.isNullOrEmpty().not() && endTime == null) {
            isValid = false
            endTimeError = "Nieprawidłowy format, wymagane: DD-MM-YYYY HH:MM"
        }

        _uiState.value = uiState.value.copy(
            readerError = readerError,
            bookError = bookError,
            startTimeError = startTimeError,
            endTimeError = endTimeError,
        )

        if (isValid.not()) return false

        val editedBorrow = Borrow(
            id = uiState.value.borrowId,
            reader = uiState.value.reader!!,
            book = uiState.value.book!!,
            startTime = startTime!!,
            endTime = endTime,
        )
        bookRepository.insert(editedBorrow)
        setup(editedBorrow)
        return true
    }

    data class UiState(
        val borrowId: Long = 0L,
        val reader: Reader? = null,
        val readerError: String = "",
        val readers: List<Reader> = emptyList(),
        val book: Book? = null,
        val bookError: String = "",
        val books: List<Book> = emptyList(),
        val startTime: String? = null,
        val startTimeError: String = "",
        val endTime: String? = null,
        val endTimeError: String = "",
        val isEditMode: Boolean = false,
    )

    sealed class Action {
        class ReaderChanged(val reader: Reader) : Action()
        class BookChanged(val book: Book) : Action()
        class StartTimeChanged(val startTime: String) : Action()
        class EndTimeChanged(val endTime: String?) : Action()
        class EditMode(val enabled: Boolean) : Action()
    }
}