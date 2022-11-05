package pl.feureth.view.console.layout.impl.reader

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.TextField
import pl.feureth.view.console.components.table.Table
import pl.feureth.view.console.components.table.TableColumn
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator

class ReaderPickerScreen(
    val onReaderSelected: (Reader) -> Unit,
) : DefaultLayout(
    "Wybór czytelnika",
    Translation.PickerInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val table = object : Table<Reader>(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 5, left = 2, right = 2)),
        columnList = listOf(
            TableColumn("ID", 1),
            TableColumn("Imię", 5),
            TableColumn("Nazwisko", 5)
        )
    ) {
        override fun convert(item: Reader): List<String> = listOf(item.id.toString(), item.name, item.surname)
        override fun onItemClick(item: Reader) {
            onReaderSelected.invoke(item)
            LayoutNavigator.back()
        }
    }

    private val queryTextField = TextField(
        drawer = contentDrawer.innerDrawer(Area.zero),
        label = "Wyszukiwanie:",
        placeholder = "Zacznij wpisywać, aby wyszukać",
        onTextChanged = { updateTable() }
    )

    override fun onStart() {
        super.onStart()
        updateTable()
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        queryTextField.draw()
        updateTable()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (queryTextField.performInput(keyStroke)) return true
        table.performInput(keyStroke)
        return true
    }

    private fun updateTable() {
        val query = queryTextField.text.lowercase()
        table.setData(
            data = bookRepository.getReaders(query = query, isBlocked = false),
            resetPointer = true
        )
    }
}