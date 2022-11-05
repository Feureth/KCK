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
import pl.feureth.view.console.components.tab.TabContainer
import pl.feureth.view.console.components.tab.TabOption
import pl.feureth.view.console.components.table.Table
import pl.feureth.view.console.components.table.TableColumn
import pl.feureth.view.console.layout.DefaultLayout

class ReaderListScreen(
    val onAddReader: () -> Unit,
    val onReaderSelected: (reader: Reader) -> Unit
) : DefaultLayout(
    "Czytelnicy",
    Translation.ListInstruction
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
        }
    }

    private val currentTableData = mutableListOf<Reader>()

    private val tabs = TabContainer(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 3)),
        menuOptions = listOf(
            TabOption(
                title = "Czytelnicy z wypożyczeniami",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    val borrows = bookRepository.getBorrows()
                    val allReaders = bookRepository.getReaders()
                    currentTableData.clear()
                    currentTableData.addAll(allReaders.filter { reader -> borrows.any { borrow -> borrow.reader == reader && borrow.endTime == null } })
                    table.setData(currentTableData, isTabChangedManually)
                }
            ),
            TabOption(
                title = "Czytelnicy aktywni",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getReaders(isBlocked = false))
                    table.setData(currentTableData, isTabChangedManually)
                }
            ),
            TabOption(
                title = "Czytelnicy zawieszeni",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getReaders(isBlocked = true))
                    table.setData(currentTableData, isTabChangedManually)
                }
            )
        )
    )

    private val queryTextField = TextField(
        drawer = contentDrawer.innerDrawer(Area.zero),
        label = "Wyszukiwanie:",
        placeholder = "Zacznij wpisywać, aby wyszukać",
        onTextChanged = { updateTable() }
    )

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        queryTextField.draw()
        tabs.draw()
        updateTable()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (keyStroke.character == '+') {
            onAddReader.invoke()
            return false
        }
        if (queryTextField.performInput(keyStroke)) return true
        if (tabs.performInput(keyStroke)) {
            updateTable()
        } else {
            table.performInput(keyStroke)
        }
        return true
    }

    private fun updateTable() {
        val query = queryTextField.text.lowercase()
        table.setData(
            data = currentTableData.filter { book ->
                query.isEmpty() ||
                        book.id.toString().contains(query, ignoreCase = true) ||
                        book.name.contains(query, ignoreCase = true) ||
                        book.surname.contains(query, ignoreCase = true)
            },
            resetPointer = false
        )
    }

}