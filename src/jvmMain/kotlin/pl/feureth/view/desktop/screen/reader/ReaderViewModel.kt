package pl.feureth.view.desktop.screen.reader

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Borrow
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.desktop.screen.ViewModel

class ReaderViewModel : ViewModel(), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun setup(readerId: Long?) {
        setup(if (readerId != null) bookRepository.getReader(readerId) else null)
    }

    private fun setup(reader: Reader?) {
        _uiState.value = UiState(
            readerId = reader?.id ?: 0L,
            name = reader?.name ?: "",
            surname = reader?.surname ?: "",
            isBlocked = reader?.isBlocked ?: false,
            isEditMode = reader == null,
            borrows = if (reader == null) emptyList() else bookRepository.getBorrows(reader = reader)
        )
    }

    fun action(action: Action) {
        when (action) {
            is Action.NameChanged -> _uiState.value = uiState.value.copy(name = action.name)
            is Action.SurnameChanged -> _uiState.value = uiState.value.copy(surname = action.surname)
            is Action.IsBlockedChanged -> _uiState.value = uiState.value.copy(isBlocked = action.isBlocked)
            is Action.EditMode -> {
                if (action.enabled) {
                    _uiState.value = uiState.value.copy(isEditMode = true)
                } else {
                    setup(readerId = uiState.value.readerId)
                }
            }
        }
    }

    fun submit(): Boolean {
        var isValid = true
        var nameError = ""
        var surnameError = ""
        if (uiState.value.name.isEmpty()) {
            isValid = false
            nameError = "Pole wymagane"
        }
        if (uiState.value.surname.isEmpty()) {
            isValid = false
            surnameError = "Pole wymagane"
        }

        _uiState.value = uiState.value.copy(
            nameError = nameError,
            surnameError = surnameError
        )

        if (isValid.not()) return false

        val editedReader = Reader(
            id = uiState.value.readerId,
            name = uiState.value.name,
            surname = uiState.value.surname,
            isBlocked = uiState.value.isBlocked,
        )
        bookRepository.insert(editedReader)
        setup(editedReader)
        return true
    }

    data class UiState(
        val readerId: Long = 0L,
        val name: String = "",
        val nameError: String = "",
        val surname: String = "",
        val surnameError: String = "",
        val isBlocked: Boolean = false,
        val isEditMode: Boolean = false,
        val borrows: List<Borrow> = emptyList(),
    )

    sealed class Action {
        class NameChanged(val name: String): Action()
        class SurnameChanged(val surname: String): Action()
        class IsBlockedChanged(val isBlocked: Boolean): Action()
        class EditMode(val enabled: Boolean): Action()
    }
}