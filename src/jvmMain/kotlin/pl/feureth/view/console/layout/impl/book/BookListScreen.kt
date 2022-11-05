package pl.feureth.view.console.layout.impl.book

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Book
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

class BookListScreen(
    val onAddBook: () -> Unit,
    val onBookSelected: (book: Book) -> Unit,
) : DefaultLayout(
    "Książki",
    Translation.ListInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    private val table = object : Table<Book>(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 5, left = 2, right = 2)),
        columnList = listOf(
            TableColumn("ID", 1),
            TableColumn("Tytuł", 8),
            TableColumn("Autor", 3)
        )
    ) {
        override fun convert(item: Book): List<String> = listOf(item.id.toString(), item.title, item.author)
        override fun onItemClick(item: Book) {
            onBookSelected.invoke(item)
        }
    }

    private val currentTableData = mutableListOf<Book>()

    private val tabs = TabContainer(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 3)),
        menuOptions = listOf(
            TabOption(
                title = "Książki wypożyczone",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getBooks(isBorrowed = true))
                    table.setData(currentTableData, isTabChangedManually)
                }
            ),
            TabOption(
                title = "Książki na stanie",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getBooks(isWithdrawn = false, isBorrowed = false))
                    table.setData(currentTableData, isTabChangedManually)
                }
            ),
            TabOption(
                title = "Książki wycofane",
                onSelected = { drawer, isTabChangedManually ->
                    drawer.clearContent()
                    currentTableData.clear()
                    currentTableData.addAll(bookRepository.getBooks(isWithdrawn = true))
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
            onAddBook.invoke()
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
                        book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true)
            },
            resetPointer = false
        )
    }
}