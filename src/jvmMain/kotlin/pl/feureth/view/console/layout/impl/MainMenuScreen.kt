package pl.feureth.view.console.layout.impl

import com.googlecode.lanterna.input.KeyStroke
import pl.feureth.view.console.Translation
import pl.feureth.view.console.common.Area
import pl.feureth.view.console.common.Drawer
import pl.feureth.view.console.components.AsciiAnimation
import pl.feureth.view.console.components.AsciiArt
import pl.feureth.view.console.components.menu.Menu
import pl.feureth.view.console.components.menu.MenuOption
import pl.feureth.view.console.layout.DefaultLayout
import pl.feureth.view.console.layout.LayoutNavigator
import pl.feureth.view.console.layout.impl.book.BookEditorScreen
import pl.feureth.view.console.layout.impl.book.BookListScreen
import pl.feureth.view.console.layout.impl.book.BookScreen
import pl.feureth.view.console.layout.impl.borrow.BorrowEditorScreen
import pl.feureth.view.console.layout.impl.borrow.BorrowListScreen
import pl.feureth.view.console.layout.impl.borrow.BorrowScreen
import pl.feureth.view.console.layout.impl.reader.ReaderEditorScreen
import pl.feureth.view.console.layout.impl.reader.ReaderListScreen
import pl.feureth.view.console.layout.impl.reader.ReaderScreen

class MainMenuScreen : DefaultLayout(
    "Menu Główne",
    Translation.MainMenuInstruction
) {

    private val logoAnimation = AsciiAnimation(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(left = 30, top = 2)),
        textFilesPath = "ascii_art/logo",
        colorStyle = Drawer.ColorStyle.YellowOnBlack
    )
    private val headerAscii = AsciiArt(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = -5)),
        textFilePath = "ascii_art/title.txt",
        colorStyle = Drawer.ColorStyle.Logo
    )

    private val bookListScreen = BookListScreen(
        onAddBook = {
            val screen = BookEditorScreen()
            LayoutNavigator.goTo(screen)
        },
        onBookSelected = {
            bookScreen.book = it
            LayoutNavigator.goTo(bookScreen)
        }
    )
    private val bookScreen = BookScreen(
        onBorrowSelected = {
            borrowScreen.borrow = it
            LayoutNavigator.goTo(borrowScreen)
        }
    )

    private val readerListScreen =
        ReaderListScreen(
            onAddReader = {
                val screen = ReaderEditorScreen()
                LayoutNavigator.goTo(screen)
            },
            onReaderSelected = {
                readerScreen.reader = it
                LayoutNavigator.goTo(readerScreen)
            }
        )
    private val readerScreen = ReaderScreen(
        onBorrowSelected = {
            borrowScreen.borrow = it
            LayoutNavigator.goTo(borrowScreen)
        }
    )

    private val borrowListScreen =
        BorrowListScreen(
            onAddBorrow = {
                val screen = BorrowEditorScreen()
                LayoutNavigator.goTo(screen)
            },
            onBorrowSelected = {
                borrowScreen.borrow = it
                LayoutNavigator.goTo(borrowScreen)
            }
        )
    private val borrowScreen = BorrowScreen()

    private val menu = Menu(
        drawer = contentDrawer.innerDrawer(Area.fromPadding(top = 5)),
        menuList = listOf(
            MenuOption(
                title = "Książki",
                onSelected = {
                    LayoutNavigator.goTo(bookListScreen)
                }
            ),
            MenuOption(
                title = "Czytelnicy",
                onSelected = {
                    LayoutNavigator.goTo(readerListScreen)
                }
            ),
            MenuOption(
                title = "Wypożyczenia",
                onSelected = {
                    LayoutNavigator.goTo(borrowListScreen)
                }
            )
        )
    )

    override fun onStart() {
        super.onStart()
        logoAnimation.start()
    }

    override fun onStop() {
        super.onStop()
        logoAnimation.stop()
    }

    override fun drawContent(drawer: Drawer) {
        super.drawContent(drawer)
        drawer.putString(0, 0, "Wersja 1.0")
        logoAnimation.draw()
        menu.draw()
        headerAscii.draw()
    }

    override fun performInput(keyStroke: KeyStroke): Boolean {
        menu.performInput(keyStroke)
        return true
    }
}