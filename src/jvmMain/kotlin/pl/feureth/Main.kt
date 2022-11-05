package pl.feureth

import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.feureth.presenter.book.BookRepository
import pl.feureth.presenter.book.LocalBookRepository
import pl.feureth.view.AppView
import pl.feureth.view.console.ConsoleView
import pl.feureth.view.desktop.DesktopView

class Main : KoinComponent {

    init {
        startKoin {
            modules(
                module {
                    single<AppView> { DesktopView() }
                    single<BookRepository> { LocalBookRepository() }
                }
            )
        }
        App()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }
}

