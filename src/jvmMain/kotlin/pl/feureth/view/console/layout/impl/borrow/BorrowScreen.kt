package pl.feureth.view.console.layout.impl.borrow

import com.googlecode.lanterna.input.KeyStroke
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.model.Borrow
import pl.feureth.presenter.book.BookRepository
import pl.feureth.toPrettyDate
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator

class BorrowScreen : DefaultLayout(
    "Podgląd wypożyczenia",
    Translation.DetailsBorrowInstruction
), KoinComponent {

    private val bookRepository: BookRepository by inject()

    lateinit var borrow: Borrow

    override fun onStart() {
        super.onStart()
        borrow = bookRepository.getBorrow(borrow.id) ?: throw IllegalArgumentException("No such borrow")
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.drawInfo(drawer, 0, "ID", borrow.id.toString())
        drawer.drawInfo(drawer, 1, "Start wypożyczenia", borrow.startTime.toPrettyDate())
        drawer.drawInfo(
            drawer,
            2,
            "Koniec wypożyczenia",
            borrow.endTime?.toPrettyDate() ?: "-".padEnd(16),
            borrow.endTime == null
        )
        drawer.drawInfo(
            drawer,
            4,
            "Czytelnik",
            "#${borrow.reader.id} - ${borrow.reader.fullName}",
            borrow.reader.isBlocked
        )
        drawer.drawInfo(
            drawer,
            5,
            "Czytelnik zablokowany",
            if (borrow.reader.isBlocked) "TAK" else "NIE",
            borrow.reader.isBlocked
        )
        drawer.drawInfo(drawer, 7, "Książka", "#${borrow.book.id} - ${borrow.book.title}", borrow.book.isWithdrawn)
        drawer.drawInfo(drawer, 8, "Autor książki", borrow.book.author)
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        if (keyStroke.character == '+') {
            val editor = BorrowEditorScreen()
            editor.borrow = borrow
            LayoutNavigator.goTo(editor)
        }
        if (keyStroke.character == '-') {
            val newTime = if (borrow.endTime == null) System.currentTimeMillis() else null
            borrow = borrow.copy(endTime = newTime)
            bookRepository.insert(borrow)
            draw()
        }
        return true
    }
}