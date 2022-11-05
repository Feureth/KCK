package pl.feureth

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.feureth.view.AppView

class App : KoinComponent {

    private val appView: AppView by inject()

    init {
        appView.start()
    }
}