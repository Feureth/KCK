package pl.feureth.view.console.layout.impl.reader

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Reader
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

class ReaderEditorScreen: DefaultLayout(
    "Edytor czytelników",
    Translation.FormInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    var reader: Reader? = null

    private val nameTextField = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 2)),
        label = "Imię",
        placeholder = "Pole wymagane",
        onTextChanged = {},
        isSelected = true,
        isSelectable = true
    )

    private val surnameTextField = TextField(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 3)),
        label = "Nazwisko",
        placeholder = "Pole wymagane",
        onTextChanged = {},
        isSelectable = true
    )

    private val isBlockedCheckbox = CheckBox(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 4)),
        label = "Zablokowana/y",
        isChecked = false,
        isSelectable = true
    )

    private val btnSubmit = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 6)),
        text = "Zapisz",
        isSelectable = true
    )

    private val form = BasicForm(
        drawer = contentDrawer,
        fields = listOf(
            nameTextField,
            surnameTextField,
            isBlockedCheckbox,
            btnSubmit
        ),
        onFieldEnter = {
            if (it == btnSubmit) {
                submit()
            }
        }
    )

    private val errorMessageText = Text(
        drawer = contentDrawer.innerDrawer(padding = Area.fromPadding(top = 8)),
        text = "",
        colorStyle = Drawer.ColorStyle.error
    )

    private fun submit() {
        val id = reader?.id ?: 0L
        val name = nameTextField.text.trim()
        val surname = surnameTextField.text.trim()
        val isBlocked = isBlockedCheckbox.isChecked
        if (name.isEmpty() || surname.isEmpty()) {
            errorMessageText.text = "Pola imienia i nazwiska są wymagane"
            draw()
            return
        }

        val reader = Reader(id = id, name = name, surname = surname, isBlocked = isBlocked)
        bookRepository.insert(reader)
        LayoutNavigator.back()
    }

    override fun onStart() {
        super.onStart()
        nameTextField.text = reader?.name ?: ""
        surnameTextField.text = reader?.surname ?: ""
        isBlockedCheckbox.isChecked = reader?.isBlocked ?: false
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.putString(0, 0, "ID - ${reader?.id ?: "NOWA POZYCJA"}")
        form.draw()
        errorMessageText.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        return form.performInput(keyStroke)
    }

}