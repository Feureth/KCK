package pl.feureth.view.console.layout.impl.borrow

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.defaultSdf
import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.Text
import pl.feureth.view.console.components.TextField
import pl.feureth.view.console.layout.BasicForm
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator
import pl.feureth.view.console.layout.impl.book.BookPickerScreen
import pl.feureth.view.console.layout.impl.reader.ReaderPickerScreen
import java.text.ParseException

class BorrowEditorScreen : DefaultLayout(
    "Edytor wypożyczeń",
    Translation.FormInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    var borrow: Borrow? = null
        set(value) {
            field = value
            selectedBook = value?.book
            selectedReader = value?.reader
        }

    var selectedBook: Book? = null
    var selectedReader: Reader? = null
    var selectedStartDate: Long? = null
    var selectedEndDate: Long? = null

    private val bookTextField = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 2)),
        label = "Książka",
        placeholder = "[Enter], aby wybrać książkę",
        onTextChanged = {},
        isSelected = true,
        isSelectable = true,
        isEditable = false
    )

    private val readerText = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 3)),
        label = "Czytelnik",
        placeholder = "[Enter], aby wybrać czytelnika",
        onTextChanged = {},
        isSelectable = true,
        isEditable = false
    )

    private val startDateTime = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 4)),
        label = "Start wypożyczenia",
        placeholder = "Pole wymagane (DD-MM-YYYY HH:MM)",
        onTextChanged = {},
        isSelectable = true
    )

    private val endDateTime = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 5)),
        label = "Koniec wypożyczenia",
        placeholder = "Pole opcjonalne (DD-MM-YYYY HH:MM)",
        onTextChanged = {},
        isSelectable = true
    )

    private val btnSubmit = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 7)),
        text = "Zapisz",
        isSelectable = true
    )

    private val errorMessageText = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 9)),
        text = "",
        colorStyle = Drawer.ColorStyle.error
    )

    private val form = BasicForm(
        drawer = contentDrawer,
        fields = listOf(
            bookTextField,
            readerText,
            startDateTime,
            endDateTime,
            btnSubmit
        ),
        onFieldEnter = {
            when (it) {
                btnSubmit -> submit()
                bookTextField -> {
                    val bookPicker = BookPickerScreen { book ->
                        this.selectedBook = book
                    }
                    LayoutNavigator.goTo(bookPicker)
                }

                readerText -> {
                    val readerPicker = ReaderPickerScreen { reader ->
                        this.selectedReader = reader
                    }
                    LayoutNavigator.goTo(readerPicker)
                }
            }
        }
    )

    private fun submit() {
        val id = borrow?.id ?: 0L
        if (selectedBook == null || selectedReader == null) {
            errorMessageText.text = "Pola tytułu i autora są wymagane"
            draw()
            return
        }

        var unixStartDateTime: Long? = null
        val isStartDateValid = try {
            unixStartDateTime = defaultSdf.parse(startDateTime.text).time
            true
        } catch (e: ParseException) {
            false
        }

        var unixEndDateTime: Long? = null
        val isEndDateValid = endDateTime.text.isEmpty() || try {
            unixEndDateTime = defaultSdf.parse(endDateTime.text).time
            true
        } catch (e: ParseException) {
            false
        }

        if (isStartDateValid.not() || isEndDateValid.not()) {
            errorMessageText.text = "Daty nie mają prawidłowego formatu (DD-MM-YYYY HH:MM)"
            draw()
            return
        }

        val borrow = Borrow(
            id = id,
            reader = selectedReader!!,
            book = selectedBook!!,
            startTime = unixStartDateTime!!,
            endTime = unixEndDateTime,
        )
        bookRepository.insert(borrow)
        LayoutNavigator.back()
    }

    override fun onStart() {
        super.onStart()
        bookTextField.text = (selectedBook ?: borrow?.book)?.title ?: ""
        readerText.text = (selectedReader ?: borrow?.reader)?.fullName ?: ""
        if (startDateTime.text.isEmpty()) {
            startDateTime.text = (selectedStartDate ?: borrow?.startTime)?.toPrettyDate() ?: ""
        }
        if (endDateTime.text.isEmpty()) {
            endDateTime.text = (selectedEndDate ?: borrow?.endTime)?.toPrettyDate() ?: ""
        }
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.putString(0, 0, "ID - ${borrow?.id ?: "NOWA POZYCJA"}")
        form.draw()
        errorMessageText.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return form.performInput(keyStroke)
    }

}