package pl.feureth.view.console.layout.impl.borrow

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Borrow
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.TextField
import pl.feureth.view.console.components.tab.TabContainer
import pl.feureth.view.console.components.tab.TabOption
import pl.feureth.view.console.components.table.Table
import pl.feureth.view.console.components.table.TableColumn
import pl.feureth.view.console.layout.DefaultLayout

class BorrowListScreen(
    val onAddBorrow: () -> Unit,
    val onBorrowSelected: (borrow: Borrow) -> Unit
) : DefaultLayout(
    "Wypożyczenia",
    Translation.ListInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val table = object : Table<Borrow>(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 5, left = 2, right = 2)),
        columnList = listOf(
            TableColumn("ID", 1),
            TableColumn("Czytelnik", 4),
            TableColumn("Książka", 4),
            TableColumn("Wypożyczone", 3),
            TableColumn("Oddane", 3),
        )
    ) {
        override fun convert(item: Borrow): List<String> = listOf(
            item.id.toString(),
            item.reader.fullName,
            item.book.title,
            item.startTime.toPrettyDate(),
            item.endTime?.toPrettyDate() ?: "-"
        )
        override fun onItemClick(item: Borrow) {
            onBorrowSelected.invoke(item)
        }
    }

    private val currentTableData = mutableListOf<Borrow>()

    private val tabs = TabContainer(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 3)),
        menuOptions = listOf(
            TabOption(
                title = "Wypożyczenia w trakcie",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getBorrows().filter { it.endTime == null })
                    table.setData(currentTableData, isTabChangedManually)
                }
            ),
            TabOption(
                title = "Historia wypożyczeń",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getBorrows().filter { it.endTime != null })
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
            onAddBorrow.invoke()
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
            data = currentTableData.filter { borrow ->
                query.isEmpty() ||
                        borrow.id.toString().contains(query, ignoreCase = true) ||
                        borrow.book.title.contains(query, ignoreCase = true) ||
                        borrow.reader.name.contains(query, ignoreCase = true) ||
                        borrow.reader.surname.contains(query, ignoreCase = true)
            },
            resetPointer = false
        )
    }
}