package pl.feureth.view.console.layout.impl.book

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Book
import pl.feureth.model.Borrow
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.table.Table
import pl.feureth.view.console.components.table.TableColumn
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator

class BookScreen(
    val onBorrowSelected: (borrow: Borrow) -> Unit
) : DefaultLayout(
    "Podgląd książki",
    Translation.DetailsInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    lateinit var book: Book

    private val borrowsTable = object : Table<Borrow>(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 8)),
        columnList = listOf(
            TableColumn("ID", 1),
            TableColumn("Czytelnik", 3),
            TableColumn("Wypożyczone", 1),
            TableColumn("Oddane", 1),
        )
    ) {
        override fun convert(item: Borrow): List<String> = listOf(
            item.id.toString(),
            item.reader.fullName,
            item.startTime.toPrettyDate(),
            item.endTime?.toPrettyDate() ?: "-"
        )

        override fun onItemClick(item: Borrow) {
            onBorrowSelected.invoke(item)
        }
    }

    override fun onStart() {
        super.onStart()
        book = bookRepository.getBook(book.id) ?: throw IllegalArgumentException("No such book")
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.drawInfo(drawer, 0, "ID", book.id.toString())
        drawer.drawInfo(drawer, 1, "Tytuł", book.title)
        drawer.drawInfo(drawer, 2, "Autor", book.author)
        drawer.drawInfo(drawer, 3, "Wycofane", if (book.isWithdrawn) "TAK" else "NIE", isError = book.isWithdrawn)
        drawer.putString(0, 6, "Historia wypożyczeń")
        borrowsTable.setData(bookRepository.getBorrows(book = book), resetPointer = false)
        borrowsTable.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (keyStroke.character == '+') {
            val editor = BookEditorScreen()
            editor.book = book
            LayoutNavigator.goTo(editor)
        }
        borrowsTable.performInput(keyStroke)
        return true
    }
}