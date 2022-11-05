package pl.feureth.view.console.layout.impl.reader

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Borrow
import pl.feureth.model.Reader
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.table.Table
import pl.feureth.view.console.components.table.TableColumn
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator

class ReaderScreen(
    val onBorrowSelected: (borrow: Borrow) -> Unit
) : DefaultLayout(
    "Podgląd czytelnika",
    Translation.DetailsInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    lateinit var reader: Reader

    private val borrowsTable = object : Table<Borrow>(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 8)),
        columnList = listOf(
            TableColumn("ID", 1),
            TableColumn("Książka", 3),
            TableColumn("Wypożyczone", 1),
            TableColumn("Oddane", 1),
        )
    ) {
        override fun convert(item: Borrow): List<String> = listOf(
            item.id.toString(),
            item.book.title,
            item.startTime.toPrettyDate(),
            item.endTime?.toPrettyDate() ?: "-"
        )

        override fun onItemClick(item: Borrow) {
            onBorrowSelected.invoke(item)
        }
    }

    override fun onStart() {
        super.onStart()
        reader = bookRepository.getReader(reader.id) ?: throw IllegalArgumentException("No such reader")
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.drawInfo(drawer, 0, "ID", reader.id.toString())
        drawer.drawInfo(drawer, 1, "Imię", reader.name)
        drawer.drawInfo(drawer, 2, "Nazwisko", reader.surname)
        drawer.drawInfo(drawer, 3, "Zawieszony", if (reader.isBlocked) "TAK" else "NIE", reader.isBlocked)
        drawer.putString(0, 6, "Historia wypożyczeń")
        borrowsTable.setData(bookRepository.getBorrows(reader = reader), resetPointer = false)
        borrowsTable.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (keyStroke.character == '+') {
            val editor = ReaderEditorScreen()
            editor.reader = reader
            LayoutNavigator.goTo(editor)
        }
        borrowsTable.performInput(keyStroke)
        return true
    }
}