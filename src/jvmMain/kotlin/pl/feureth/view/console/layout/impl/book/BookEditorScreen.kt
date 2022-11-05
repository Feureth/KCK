package pl.feureth.view.console.layout.impl.book

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Book
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.CheckBox
import pl.feureth.view.console.components.Text
import pl.feureth.view.console.components.TextField
import pl.feureth.view.console.layout.BasicForm
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator

class BookEditorScreen : DefaultLayout(
    "Edytor książek",
    Translation.FormInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    var book: Book? = null

    private val titleTextField = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 2)),
        label = "Tytuł",
        placeholder = "Pole wymagane",
        onTextChanged = {},
        isSelected = true,
        isSelectable = true
    )

    private val authorTextField = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 3)),
        label = "Autor",
        placeholder = "Pole wymagane",
        onTextChanged = {},
        isSelectable = true
    )

    private val isWithdrawnCheckbox = CheckBox(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 4)),
        label = "Wycofane",
        isChecked = false,
        isSelectable = true
    )

    private val btnSubmit = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 6)),
        text = "Zapisz",
        isSelectable = true
    )

    private val errorMessageText = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 8)),
        text = "",
        colorStyle = Drawer.ColorStyle.error
    )

    private val form = BasicForm(
        drawer = contentDrawer,
        fields = listOf(
            titleTextField,
            authorTextField,
            isWithdrawnCheckbox,
            btnSubmit
        ),
        onFieldEnter = {
            if (it == btnSubmit) submit()
        }
    )

    private fun submit() {
        val id = book?.id ?: 0L
        val title = titleTextField.text.trim()
        val author = authorTextField.text.trim()
        val isWithdrawn = isWithdrawnCheckbox.isChecked
        if (title.isEmpty() || author.isEmpty()) {
            errorMessageText.text = "Pola tytułu i autora są wymagane"
            draw()
            return
        }

        val book = Book(id = id, title = title, author = author, isWithdrawn = isWithdrawn)
        bookRepository.insert(book)
        LayoutNavigator.back()
    }

    override fun onStart() {
        super.onStart()
        titleTextField.text = book?.title ?: ""
        authorTextField.text = book?.author ?: ""
        isWithdrawnCheckbox.isChecked = book?.isWithdrawn ?: false
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.putString(0, 0, "ID - ${book?.id ?: "NOWA POZYCJA"}")
        form.draw()
        errorMessageText.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return form.performInput(keyStroke)
    }

}